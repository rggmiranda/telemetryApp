package com.rggmiranda.telemetryApp.model;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.Instant;

@Data
@RepositoryRestResource(exported = false)
@NoArgsConstructor
@Document(collection = "telemetry")
public class Telemetry {

    @Id
    private String id;

    private Instant timestamp;

    private double value;

    @DBRef
    private Sensor sensor;

    public Telemetry(Instant timestamp, Sensor theSensor, double value) {
        this.timestamp = timestamp;
        this.sensor = theSensor;
        this.value = value;
    }

}
