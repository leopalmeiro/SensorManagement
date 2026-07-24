package com.wharehouse.sensormanagement.data;

import com.wharehouse.sensormanagement.model.SensorType;

import java.time.Instant;

public record SensorData(
        String id,
        double value,
        Instant timestamp,
        SensorType sensorType) { }
