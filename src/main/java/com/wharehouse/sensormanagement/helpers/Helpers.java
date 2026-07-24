package com.wharehouse.sensormanagement.helpers;

import com.wharehouse.sensormanagement.config.SensorThresholdConfig;
import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.model.SensorType;
import org.springframework.stereotype.Component;

@Component
public class Helpers {

    private final SensorThresholdConfig sensorThresholdConfig;

    public Helpers(SensorThresholdConfig sensorThresholdConfig) {
        this.sensorThresholdConfig = sensorThresholdConfig;
    }

    /**
     * Determines whether the given sensor reading exceeds its configured threshold.
     *
     * <p>The threshold used depends on the sensor type:</p>
     * <ul>
     *   <li>Humidity sensors are compared against {@code HUMIDITY_THRESHOLD}.</li>
     *   <li>All other sensor types are compared against {@code TEMPERATURE_THRESHOLD}.</li>
     * </ul>
     *
     * @param sensorData the sensor data containing the sensor type and measured value
     * @return {@code true} if the sensor value exceeds its corresponding threshold;
     *         {@code false} otherwise
     */
    public boolean hasThresholdPassed(SensorData sensorData){
        if(sensorData.sensorType().equals(SensorType.HUMIDITY)){
            return sensorData.value() > sensorThresholdConfig.getHumidityThreshold();
        } else {
            return sensorData.value() > sensorThresholdConfig.getTemperatureThreshold();
        }
    }
}
