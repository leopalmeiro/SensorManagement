package com.wharehouse.sensormanagement.validators;

import com.wharehouse.sensormanagement.constants.Constants;
import com.wharehouse.sensormanagement.exceptions.InvalidSensorDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SensorDataValidator {

    private static final Logger log = LoggerFactory.getLogger(SensorDataValidator.class);

    public String validateSensorData(String payload) {
        log.info("Called validateSensorData method");
        if (payload == null || payload.isBlank()) {
            throw new InvalidSensorDataException(Constants.SENSOR_PAYLOAD_IS_EMPTY);
        }

        try {
            String[] fields = payload.split(Constants.SEMICOLON);

            boolean hasSensorId = false;
            boolean hasValue = false;

            for (String field : fields) {
                String[] keyValue = field.trim().split(Constants.EQUAL);

                if (keyValue.length != 2) {
                    throw new InvalidSensorDataException(
                            "Invalid field format: " + field);
                }

                switch (keyValue[0].trim()) {
                    case Constants.SENSOR_ID:
                        hasSensorId = true;
                        break;

                    case Constants.VALUE:
                        Double.parseDouble(keyValue[1].trim());
                        hasValue = true;
                        break;

                    default:
                        throw new InvalidSensorDataException(
                                "Unknown field: " + keyValue[0]);
                }
            }

            if (!hasSensorId || !hasValue) {
                throw new InvalidSensorDataException(
                        Constants.MISSING_REQUIRED_FIELDS);
            }

            return payload;

        } catch (NumberFormatException e) {
            throw new InvalidSensorDataException(
                    Constants.SENSOR_VALUE_MUST_BE_NUMERIC, e);
        }
    }
}
