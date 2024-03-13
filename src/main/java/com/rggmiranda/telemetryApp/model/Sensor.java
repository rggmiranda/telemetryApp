package com.rggmiranda.telemetryApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@Entity
@Data
@RepositoryRestResource(exported = false)
@Table(name="sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "device_id")
    @JsonIgnore
    private Device device;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensorType_id")
    private SensorType sensorType;

    public Sensor(String name, SensorType sensorType) {
        this.name = name;
        this.sensorType = sensorType;
    }

    public Sensor() {
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Sensor) {
            if(((Sensor) o).getName().equals(this.getName()) && ((Sensor) o).getDevice().equals(this.getDevice())){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
