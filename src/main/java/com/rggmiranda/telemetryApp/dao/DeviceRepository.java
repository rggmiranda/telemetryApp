package com.rggmiranda.telemetryApp.dao;

import com.rggmiranda.telemetryApp.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Integer> {
    List<Device> findByUserName(String userName);
}
