package com.wharehouse.sensormanagement.model;

import java.time.Instant;

public record SensorData(
        String id,
        double value,
        Instant timestamp,
        SensorType sensorType) { }
