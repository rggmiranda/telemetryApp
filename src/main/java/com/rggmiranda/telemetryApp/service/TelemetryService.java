package com.rggmiranda.telemetryApp.service;

import com.rggmiranda.telemetryApp.dao.TelemetryReactiveRepository;
import com.rggmiranda.telemetryApp.dao.TelemetryRepository;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TelemetryService implements ITelemetryService {



    private TelemetryRepository telemetryDataRepository;

    private TelemetryReactiveRepository telemetryReactiveRepository;

    @Autowired
    public TelemetryService(TelemetryRepository theTelemetryDataRepository,TelemetryReactiveRepository theTelemetryReactiveRepository) {
        this.telemetryDataRepository=theTelemetryDataRepository;
        this.telemetryReactiveRepository=theTelemetryReactiveRepository;
    }


    @Override
    public List<Telemetry> getTelemetryData(Sensor sensor, Instant start, Instant end) {
        return telemetryDataRepository.findBySensorAndTimestampBetween(sensor, start, end);
    }

    @Override
    public Telemetry getLastTelemetry(Sensor sensor) {
        Optional<Telemetry> optionalTelemetry = telemetryDataRepository.findTopBySensorOrderByTimestampDesc(sensor);
        return optionalTelemetry.orElse(null);
    }

    @Override
    public Telemetry saveTelemetryData(Telemetry telemetryData) {
        return telemetryDataRepository.save(telemetryData);
    }

    @Override
    public Flux<List<Telemetry>> getLast3TelemetryReactive(Sensor sensor) {
        return Flux.defer(() -> {
            List<Telemetry> telemetries = telemetryDataRepository.findTop3BySensorOrderByTimestampDesc(sensor);

            return Flux.just(telemetries);
        });
    }

}

