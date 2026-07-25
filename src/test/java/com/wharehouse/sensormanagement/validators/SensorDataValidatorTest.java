package com.wharehouse.sensormanagement.validators;

import static org.junit.jupiter.api.Assertions.*;

import com.wharehouse.sensormanagement.constants.Constants;
import com.wharehouse.sensormanagement.exceptions.InvalidSensorDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorDataValidatorTest {

    private SensorDataValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SensorDataValidator();
    }

    @Test
    void shouldReturnPayloadWhenValid() {
        String payload = "sensor_id=123;value=25.5";

        String result = validator.validateSensorData(payload);

        assertEquals(payload, result);
    }

    @Test
    void shouldThrowExceptionWhenPayloadIsNull() {
        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(null)
        );

        assertEquals(Constants.SENSOR_PAYLOAD_IS_EMPTY, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPayloadIsBlank() {
        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData("   ")
        );

        assertEquals(Constants.SENSOR_PAYLOAD_IS_EMPTY, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFieldFormatIsInvalid() {
        String payload = "sensor_id=123;value";

        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(payload)
        );

        assertEquals("Invalid field format: value", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUnknownFieldExists() {
        String payload = "sensor_id=123;temperature=20";

        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(payload)
        );

        assertEquals("Unknown field: temperature", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhensensor_idIsMissing() {
        String payload = "value=20.5";

        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(payload)
        );

        assertEquals(Constants.MISSING_REQUIRED_FIELDS, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsMissing() {
        String payload = "sensor_id=123";

        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(payload)
        );

        assertEquals(Constants.MISSING_REQUIRED_FIELDS, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNotNumeric() {
        String payload = "sensor_id=123;value=abc";

        InvalidSensorDataException exception = assertThrows(
                InvalidSensorDataException.class,
                () -> validator.validateSensorData(payload)
        );

        assertEquals(Constants.SENSOR_VALUE_MUST_BE_NUMERIC, exception.getMessage());
        assertNotNull(exception.getCause());
        assertInstanceOf(NumberFormatException.class, exception.getCause());
    }

    @Test
    void shouldAcceptNegativeNumericValue() {
        String payload = "sensor_id=123;value=-12.7";

        String result = validator.validateSensorData(payload);

        assertEquals(payload, result);
    }

    @Test
    void shouldAcceptIntegerValue() {
        String payload = "sensor_id=123;value=42";

        String result = validator.validateSensorData(payload);

        assertEquals(payload, result);
    }

    @Test
    void shouldIgnoreExtraSpaces() {
        String payload = " sensor_id = 123 ; value = 20.5 ";

        String result = validator.validateSensorData(payload);

        assertEquals(payload, result);
    }
}