package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.Telemetry;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

public interface ITelemetryService {

    List<Telemetry> getTelemetryData(Sensor sensor, Instant start, Instant end);

    Telemetry getLastTelemetry(Sensor sensor);

    Telemetry saveTelemetryData(Telemetry telemetryData);

//    Flux<Telemetry> getLast3TelemetryReactive(Sensor sensor);
    public Flux<List<Telemetry>> getLast3TelemetryReactive(Sensor sensor);
}

