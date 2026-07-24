package com.wharehouse.sensormanagement.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.model.SensorType;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Mappers {

    public static SensorData mapperRecivedDataJsonToSensorData(String payload){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            // 2. Use readValue instead of convertValue for raw JSON Strings
            SensorData sensorData = mapper.readValue(payload, SensorData.class);

            // If you need to attach the sensorType after parsing:
            // sensorData.setSensorType(sensorType);

            return sensorData;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON payload", e);
        }
    }

    public static String mapperReceivedDataToJsonSensorData(String payload, SensorType sensorType){

        Map<String, String> keyValueMap = Arrays.stream(
                payload.split(";"))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> parts[1].trim()
                ));

        SensorData sensorData = new SensorData(
                keyValueMap.get("sensor_id"),
                Double.parseDouble(keyValueMap.get("value")), Instant.now(), sensorType);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );
        try {
            return mapper.writeValueAsString(sensorData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
