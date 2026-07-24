package com.wharehouse.sensormanagement.exceptions;

public class InvalidSensorDataException extends RuntimeException {

    public InvalidSensorDataException(String message) {
        super(message);
    }

    public InvalidSensorDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
