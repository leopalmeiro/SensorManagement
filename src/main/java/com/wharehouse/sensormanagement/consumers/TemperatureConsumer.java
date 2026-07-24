package com.wharehouse.sensormanagement.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TemperatureConsumer {


    @RabbitListener(queues = "temperature.queue")
    public void receive(String message) {

        System.out.println(
                "Temperature received from consumer: " + message
        );
    }
}