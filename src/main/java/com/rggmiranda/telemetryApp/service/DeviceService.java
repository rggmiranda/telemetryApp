package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.dao.DeviceRepository;
import com.rggmiranda.telemetryApp.dao.SensorRepository;
import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService implements IDeviceService{
    DeviceRepository deviceRepository;
    @Autowired
    public DeviceService(DeviceRepository theDeviceRepository){
        deviceRepository=theDeviceRepository;
    }

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device findById(int id) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        return optionalDevice.orElse(null);
    }

    @Override
    public Device save(Device theDevice) {
        return deviceRepository.save(theDevice);
    }

    @Override
    public void deleteById(int id) {
        if(findById(id)!=null){
            deviceRepository.deleteById(id);
        }
    }

    @Override
    public List<Device> findByUserName(String userName) {
        return deviceRepository.findByUserName(userName);
    }
}
