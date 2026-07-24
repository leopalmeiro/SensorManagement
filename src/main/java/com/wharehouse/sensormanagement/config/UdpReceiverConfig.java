package com.wharehouse.sensormanagement.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.mappers.Mappers;
import com.wharehouse.sensormanagement.model.SensorType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Udp;

@Configuration
public class UdpReceiverConfig {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;


    public UdpReceiverConfig(
            RabbitTemplate rabbitTemplate,
            ObjectMapper mapper
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }


    @Bean
    IntegrationFlow temperatureSensorUdpIn() {

        return IntegrationFlow
                .from(Udp.inboundAdapter(3344))
                .transform(byte[].class, String::new)
                .transform(String.class, payload -> Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.TEMPERATURE))
                .handle((payload, headers) -> {
                    rabbitTemplate.convertAndSend(
                            "sensor.exchange",
                            "humidity",
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
                .transform(String.class, payload -> Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.HUMIDITY))
                .handle((payload, headers) -> {
                    rabbitTemplate.convertAndSend(
                            "sensor.exchange",
                            "humidity",
                            payload
                    );

                    return null;
                })
                .get();
    }

}
