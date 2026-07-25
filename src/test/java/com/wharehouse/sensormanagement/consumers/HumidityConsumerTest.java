package com.wharehouse.sensormanagement.consumers;

import com.wharehouse.sensormanagement.mappers.Mappers;
import com.wharehouse.sensormanagement.model.SensorType;
import com.wharehouse.sensormanagement.services.WarehouseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HumidityConsumerTest {
    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private HumidityConsumer humidityConsumer;

    @Test
    void shouldCallHumidityConsumer() {
        String payload = "sensor_id=h1; value=30";
        String jsonResult = Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.HUMIDITY);
        humidityConsumer.receive(jsonResult);

        verify(warehouseService)
                .handlerReceiverInformation(jsonResult);
    }
}