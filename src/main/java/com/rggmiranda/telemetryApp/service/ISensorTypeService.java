package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.SensorType;

import java.util.List;

public interface ISensorTypeService {
    List<SensorType> findAll();

    SensorType findById(int theId);

    SensorType save(SensorType sensorType);

    void deleteById(int theId);


}
