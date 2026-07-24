package com.wharehouse.sensormanagement.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Udp;

@Configuration
public class UdpReceiverConfig {

    private final RabbitTemplate rabbitTemplate;


    public UdpReceiverConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Bean
    IntegrationFlow temperatureSensorUdpIn() {

        return IntegrationFlow
                .from(Udp.inboundAdapter(3344))
                .transform(byte[].class, String::new)
                .handle((payload, headers) -> {

                    rabbitTemplate.convertAndSend(
                            "sensor.exchange",
                            "temperature",
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
