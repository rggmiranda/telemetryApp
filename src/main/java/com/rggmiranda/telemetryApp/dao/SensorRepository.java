package com.rggmiranda.telemetryApp.dao;

import com.rggmiranda.telemetryApp.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor,Integer> {
}
