package com.rggmiranda.telemetryApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@Entity
@Data
//@RepositoryRestResource(exported = false)
@NoArgsConstructor
@Table(name="sensorType")
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "sensorType", cascade = CascadeType.PERSIST)
    private List<Sensor> sensors;

    public SensorType(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
