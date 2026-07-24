package com.wharehouse.sensormanagement.helpers;

import com.wharehouse.sensormanagement.data.SensorData;
import com.wharehouse.sensormanagement.model.SensorType;

public class Helpers {
    public static final double TEMPERATURE_THRESHOLD = 30;
    public static final double HUMIDITY_THRESHOLD = 40;

    public static boolean hasThresholdPassed(SensorData sensorData){
        if(sensorData.sensorType().equals(SensorType.HUMIDITY)){
            if (sensorData.value() > HUMIDITY_THRESHOLD) {
                System.out.println("alarm of " + sensorData.sensorType() );
                return true;
            }
        } else {
            if (sensorData.value() > TEMPERATURE_THRESHOLD) {
                System.out.println("alarm of " + sensorData.sensorType() );
                return true;
            }
        }
        return false;
    }
}
