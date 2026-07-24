package com.wharehouse.sensormanagement.services;

import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.helpers.Helpers;
import com.wharehouse.sensormanagement.mappers.Mappers;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {

    public void handlerReceiverInformation(String message){
            SensorData sensorData = Mappers.mapperRecivedDataJsonToSensorData(message);
            boolean hasThresholdPassed = Helpers.hasThresholdPassed(sensorData);

            if (hasThresholdPassed) {
                System.out.println("Alerttt " + sensorData.sensorType());
            }
    }
}
