package com.wharehouse.sensormanagement.consumers;

import com.wharehouse.sensormanagement.services.WarehouseService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class HumidityConsumer {

    private final WarehouseService warehouseService;

    public HumidityConsumer(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @RabbitListener(queues = "humidity.queue")
    public void receive(String message) {

        warehouseService.handlerReceiverInformation(message);
    }
}