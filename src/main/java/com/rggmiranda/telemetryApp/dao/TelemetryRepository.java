package com.rggmiranda.telemetryApp.dao;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.Telemetry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

    public interface TelemetryRepository extends MongoRepository<Telemetry,String> {
    List<Telemetry> findBySensorAndTimestampBetween(Sensor sensor, Instant start, Instant end);
    Optional<Telemetry> findTopBySensorOrderByTimestampDesc(Sensor sensor);
    List<Telemetry> findTop3BySensorOrderByTimestampDesc(Sensor sensor);
}
