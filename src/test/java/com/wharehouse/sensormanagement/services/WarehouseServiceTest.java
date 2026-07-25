package com.wharehouse.sensormanagement.services;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wharehouse.sensormanagement.helpers.Helpers;
import com.wharehouse.sensormanagement.model.SensorData;
import com.wharehouse.sensormanagement.model.SensorType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private Helpers helpers;

    private WarehouseService warehouseService;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(WarehouseService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        warehouseService = new WarehouseService(helpers);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(listAppender);
    }

    @Test
    void shouldLogAlertWhenThresholdIsPassedWithSensorTypeTemperature() throws JsonProcessingException {
        //Arrange
        SensorData sensorData = new SensorData(
                "T1",
                55,
                Instant.now(),
                SensorType.TEMPERATURE
                );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );
        String sensorDataJson = mapper.writeValueAsString(sensorData);
        when(helpers.hasThresholdPassed(sensorData)).thenReturn(true);
        //act
        warehouseService.handlerReceiverInformation(sensorDataJson);

        boolean alertMessageFound = listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.ERROR &&
                        event.getFormattedMessage().contains("ALERT: The TEMPERATURE Sensor with id : T1 has exceeded the threshold."));
        //assert
        assertThat(alertMessageFound).isTrue();
    }

    @Test
    void shouldLogAlertWhenThresholdNotPassedWithSensorTypeTemperature() throws JsonProcessingException {
        //Arrange
        SensorData sensorData = new SensorData(
                "T1",
                10,
                Instant.now(),
                SensorType.TEMPERATURE
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );
        String sensorDataJson = mapper.writeValueAsString(sensorData);
        when(helpers.hasThresholdPassed(sensorData)).thenReturn(false);
        //act
        warehouseService.handlerReceiverInformation(sensorDataJson);

        boolean infoMessageFound = listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.INFO &&
                        event.getFormattedMessage().contains("The TEMPERATURE Sensor with id : T1 is heathy"));
        //assert
        assertThat(infoMessageFound).isTrue();
    }
}