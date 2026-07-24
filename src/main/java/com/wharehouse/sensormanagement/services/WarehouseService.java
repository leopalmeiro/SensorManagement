package com.wharehouse.sensormanagement.services;

import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.helpers.Helpers;
import com.wharehouse.sensormanagement.mappers.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);
    private final Helpers helpers;

    public WarehouseService(Helpers helpers) {
        this.helpers = helpers;
    }

    /**
     * Processes the received sensor information, evaluates whether the sensor
     * has exceeded its configured threshold, and logs the appropriate status.
     *
     * <p>If the sensor value exceeds the threshold, an alert is logged at the
     * error level. Otherwise, an informational message indicating that the
     * sensor is healthy is logged.</p>
     *
     * @param message the JSON message containing the sensor data
     */
    public void handlerReceiverInformation(String message){
            log.info("Called handlerReceiverInformation method");
            SensorData sensorData = Mappers.mapperReceivedDataJsonToSensorData(message);
            boolean hasThresholdPassed = helpers.hasThresholdPassed(sensorData);
            if (hasThresholdPassed) {
                log.error("ALERT: The {} Sensor with id : {} has exceeded the threshold.", sensorData.sensorType(), sensorData.id());
            } else {
                log.info("The {} Sensor with id : {} is heathy", sensorData.sensorType(), sensorData.id());
            }
    }
}
