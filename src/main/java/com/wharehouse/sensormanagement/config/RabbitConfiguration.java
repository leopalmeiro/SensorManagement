package com.wharehouse.sensormanagement.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String SENSOR_EXCHANGE = "sensor.exchange";

    @Bean
    DirectExchange sensorExchange() {
        return new DirectExchange(SENSOR_EXCHANGE);
    }

    @Bean
    Queue temperatureQueue() {
        return new Queue("temperature.queue");
    }

    @Bean
    Queue humidityQueue() {
        return new Queue("humidity.queue");
    }

    @Bean
    Binding temperatureBinding(
            Queue temperatureQueue,
            DirectExchange sensorExchange) {

        return BindingBuilder
                .bind(temperatureQueue)
                .to(sensorExchange)
                .with("temperature");
    }

    @Bean
    Binding humidityBinding(
            Queue humidityQueue,
            DirectExchange sensorExchange) {

        return BindingBuilder
                .bind(humidityQueue)
                .to(sensorExchange)
                .with("humidity");
    }
}
