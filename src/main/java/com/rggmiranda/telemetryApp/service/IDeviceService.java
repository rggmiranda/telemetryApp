package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.model.Device;

import java.util.List;

public interface IDeviceService {
    List<Device> findAll();

    List<Device> findByUserName(String userName);

    Device findById(int theId);

    Device save(Device theDevice);

    void deleteById(int theId);


}
