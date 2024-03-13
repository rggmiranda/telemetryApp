package com.rggmiranda.telemetryApp.dao;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.Telemetry;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

    public interface TelemetryReactiveRepository extends ReactiveMongoRepository<Telemetry,String> {
    Flux<Telemetry> findBySensor(Sensor sensor);
    Flux<Telemetry> findTop3BySensorOrderByTimestampDesc(Sensor sensor);
}