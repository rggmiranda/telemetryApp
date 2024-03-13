package com.rggmiranda.telemetryApp.controller;

import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.Telemetry;
import com.rggmiranda.telemetryApp.service.DeviceService;
import com.rggmiranda.telemetryApp.service.TelemetryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RESTcontroller{


    private DeviceService deviceService;

    private TelemetryService telemetryService;

    @Autowired
    public RESTcontroller(DeviceService theDeviceService,TelemetryService theTelemetryService) {
        this.deviceService=theDeviceService;
        this.telemetryService=theTelemetryService;
    }

    @GetMapping("/devices/{id}")
    public Device getDevice(@PathVariable int id){
        return deviceService.findById(id);
    }

    @GetMapping("/devices")
    public List<Device> getAllDevices(){
        return deviceService.findAll();
    }


    @PostMapping("/telemetry")

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the Item to be created",
            required = true,
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    value = "{\"telemetryValue\": 25.3,"
                                            + "\"sensorId\": 2,"
                                            + "\"deviceId\": 1,"
                                            + "\"apiKey\": \"ebec95fb-0c06-4da3-af37-2ef54a20bc07\""
                                            + "}"
                            )}))
    public Telemetry postTelemetry(@RequestBody Map<String, Object> requestBody){
//        System.out.println("Received telemetry data: " + requestBody);
        if (requestBody.containsKey("telemetryValue")&&
            requestBody.containsKey("sensorId")&&
            requestBody.containsKey("deviceId")&&
            requestBody.containsKey("apiKey"))
            {
                int deviceId = (int) requestBody.get("deviceId");
                int sensorId = (int) requestBody.get("sensorId");
                double telemetryValue = (double) requestBody.get("telemetryValue");

                Sensor sensor = deviceService.findById(deviceId).getSensorById(sensorId);
                if(sensor!=null){
                    Telemetry telemetry = new Telemetry(Instant.now(),sensor,telemetryValue);
                    return telemetryService.saveTelemetryData(telemetry);
                }
        }
        return null;
    }


    @GetMapping("/telemetry-stream")
    public Flux<List<Telemetry>> streamTelemetryData(
            @RequestParam("sensorId") Integer sensorId,
            @RequestParam("deviceId") Integer deviceId) {

        Sensor sensor = deviceService.findById(deviceId).getSensorById(sensorId);

        Flux<List<Telemetry>> telemetryFlux = telemetryService.getLast3TelemetryReactive(sensor);

        return telemetryFlux;

    }

}
