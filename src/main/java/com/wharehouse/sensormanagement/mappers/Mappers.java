package com.wharehouse.sensormanagement.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.model.SensorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Mappers {

    public static final String FAILED_TO_PARSE_JSON_PAYLOAD = "Failed to parse JSON payload";
    private static final Logger log = LoggerFactory.getLogger(Mappers.class);

    /**
     * Method to map Received Data formated as Json to Sensor Data
     * @param payload the JSON string containing the received sensor information
     * @return SensorData
     */
    public static SensorData mapperReceivedDataJsonToSensorData(String payload) {
        log.info("Called mapperReceivedDataJsonToSensorData method");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(payload, SensorData.class);
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_PARSE_JSON_PAYLOAD, e);
        }
    }

    /**
     * Method to map payload and sensor type into a Json
     * @param payload string data formated as sensor_id=h1; value=40
     * @param sensorType type of sensor
     * @return string as json format
     */
    public static String mapperReceivedDataToJsonSensorData(String payload, SensorType sensorType){
        log.info("Called mapperReceivedDataToJsonSensorData method");
        Map<String, String> keyValueMap = Arrays.stream(
                payload.split(";"))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> parts[1].trim()
                ));

        //get the converted information then create sensor data object
        SensorData sensorData = new SensorData(
                keyValueMap.get("sensor_id"),
                Double.parseDouble(keyValueMap.get("value")),
                Instant.now(),
                sensorType);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );
        try {
            return mapper.writeValueAsString(sensorData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON payload", e);
        }
    }
}
