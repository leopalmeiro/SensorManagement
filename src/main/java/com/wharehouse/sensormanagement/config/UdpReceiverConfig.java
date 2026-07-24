package com.wharehouse.sensormanagement.config;

import com.rabbitmq.client.amqp.Message;
import com.wharehouse.sensormanagement.constants.Constants;
import com.wharehouse.sensormanagement.mappers.Mappers;
import com.wharehouse.sensormanagement.model.SensorType;
import com.wharehouse.sensormanagement.validators.SensorDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Udp;

@Configuration
public class UdpReceiverConfig {
    private static final Logger log = LoggerFactory.getLogger(UdpReceiverConfig.class);

    private final RabbitTemplate rabbitTemplate;
    private final SensorDataValidator sensorDataValidator;

    public UdpReceiverConfig(
            RabbitTemplate rabbitTemplate,
            SensorDataValidator sensorDataValidator
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.sensorDataValidator = sensorDataValidator;
    }

    @Bean
    IntegrationFlow temperatureSensorUdpIn() {
        return IntegrationFlow
                .from(Udp.inboundAdapter(3344))
                .transform(byte[].class, String::new)
                .transform(String.class, sensorDataValidator::validateSensorData)
                .transform(String.class, payload -> Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.TEMPERATURE))
                .handle((payload, headers) -> {
                    log.info(Constants.MESSAGE_RECEIVED_WITH_PAYLOAD_AND_MESSAGE_HEADERS, payload, headers);
                    rabbitTemplate.convertAndSend(
                            Constants.SENSOR_EXCHANGE,
                            Constants.TEMPERATURE,
                            payload
                    );
                    return null;
                })
                .get();
    }

    @Bean
    IntegrationFlow humiditySensorUdpIn() {
        return IntegrationFlow
                .from(Udp.inboundAdapter(3355))
                .transform(byte[].class, String::new)
                .transform(String.class, sensorDataValidator::validateSensorData)
                .transform(String.class, payload -> Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.HUMIDITY))
                .handle((payload, headers) -> {
                    log.info(Constants.MESSAGE_RECEIVED_WITH_PAYLOAD_AND_MESSAGE_HEADERS, payload, headers);
                    rabbitTemplate.convertAndSend(
                            Constants.SENSOR_EXCHANGE,
                            Constants.HUMIDITY,
                            payload
                    );
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow errorHandlingFlow() {
        return IntegrationFlow
                .from(Constants.ERROR_CHANNEL)
                .handle(Message.class, (message, headers) -> {
                    log.info(Constants.ERROR_RECEIVED_WITH_MESSAGE_AND_HEADERS, message, headers);
                    Throwable exception =
                            (Throwable) message;
                    log.warn(Constants.INVALID_SENSOR_MESSAGE_RECEIVED,
                            exception.getMessage());
                    return null;
                })
                .get();
    }

}
