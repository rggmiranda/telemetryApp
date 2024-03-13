package com.rggmiranda.telemetryApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@Setter
@RepositoryRestResource(exported = false)
@Table(name="device")

public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    private String apiKey;

    private String userName;

    @OneToMany(
            mappedBy = "device",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch=FetchType.EAGER)
    private Set<Sensor> sensors = new HashSet<>();

    public Device(String name) {
        this.name = name;
    }

    public Device() {}

    public void addSensor(Sensor sensor) {
        sensor.setDevice(this);
        if (!sensors.add(sensor)){
            sensor.setDevice(null);
        }
    }

    public void updateSensor(Sensor sensor) {

        for (Sensor item : sensors){
            if (item.getId().equals(sensor.getId()))
            {
                sensors.remove(item);
                break;
            }
        }
        sensor.setDevice(this);
        if (!sensors.add(sensor)){
            sensor.setDevice(null);
        }
    }


    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.setDevice(null);
    }

    public Sensor getSensorById(int id){
        Optional<Sensor> foundSensor = sensors.stream()
                .filter(sensor -> sensor.getId() != null && sensor.getId() == id)
                .findFirst();

        return foundSensor.orElse(null);
    }

    @Override
    public String toString(){
        return this.getName() + " " + this.getSensors();
    }
}
