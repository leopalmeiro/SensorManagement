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
class TemperatureConsumerTest {
    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private TemperatureConsumer temperatureConsumer;

    @Test
    void shouldCallTemperatureConsumer() {
        String payload = "sensor_id=t1; value=30";
        String jsonResult = Mappers.mapperReceivedDataToJsonSensorData(payload, SensorType.TEMPERATURE);
        temperatureConsumer.receive(jsonResult);

        verify(warehouseService)
                .handlerReceiverInformation(jsonResult);
    }
}