package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.dao.DeviceRepository;
import com.rggmiranda.telemetryApp.dao.SensorTypeRepository;
import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorTypeService implements ISensorTypeService{
    SensorTypeRepository sensorTypeRepository;
    @Autowired
    public SensorTypeService(SensorTypeRepository theSensorTypeRepository){
        sensorTypeRepository=theSensorTypeRepository;
    }

    @Override
    public List<SensorType> findAll() {
        return sensorTypeRepository.findAll();
    }

    @Override
    public SensorType findById(int id) {
        Optional<SensorType> optionalSensorType = sensorTypeRepository.findById(id);
        return optionalSensorType.orElse(null);
    }

    @Override
    public SensorType save(SensorType sensorType) {
        return sensorTypeRepository.save(sensorType);
    }

    @Override
    public void deleteById(int id) {
        if(findById(id)!=null){
            sensorTypeRepository.deleteById(id);
        }
    }
}
