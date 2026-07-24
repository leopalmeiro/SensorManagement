package com.wharehouse.sensormanagement.consumers;

import com.wharehouse.sensormanagement.services.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class HumidityConsumer {

    private static final Logger log = LoggerFactory.getLogger(HumidityConsumer.class);
    private final WarehouseService warehouseService;

    public HumidityConsumer(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @RabbitListener(queues = "humidity.queue")
    public void receive(String message) {
        log.info("Received message in the queue {}", "humidity.queue");
        warehouseService.handlerReceiverInformation(message);
    }
}