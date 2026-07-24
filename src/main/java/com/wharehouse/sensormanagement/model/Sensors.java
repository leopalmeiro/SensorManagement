package com.wharehouse.sensormanagement.model;

public record Sensors(
        String sensorId,
        Double value,
        SensorType type
) {};


