package com.wharehouse.sensormanagement.consumers;

import com.wharehouse.sensormanagement.services.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TemperatureConsumer {
    private static final Logger log = LoggerFactory.getLogger(TemperatureConsumer.class);
    private final WarehouseService warehouseService;

    public TemperatureConsumer(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @RabbitListener(queues = "temperature.queue")
    public void receive(String message) {
        log.info("Received message in the queue {}", "temperature.queue");
        warehouseService.handlerReceiverInformation(message);
    }
}