package com.rggmiranda.telemetryApp.dao;

import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(exported = false)
public interface SensorTypeRepository extends JpaRepository<SensorType,Integer> {
}



