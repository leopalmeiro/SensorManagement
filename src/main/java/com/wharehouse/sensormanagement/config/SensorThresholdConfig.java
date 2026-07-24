package com.wharehouse.sensormanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sensor")
public class SensorThresholdConfig {

    @Value("${sensor.temperature.threshold}")
    private double temperatureThreshold;

    @Value("${sensor.humidity.threshold}")
    private double humidityThreshold;

    public double getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public double getHumidityThreshold() {
        return humidityThreshold;
    }
}
