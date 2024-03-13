package com.rggmiranda.telemetryApp;

import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.SensorType;
import com.rggmiranda.telemetryApp.service.DeviceService;
import com.rggmiranda.telemetryApp.service.SensorTypeService;
import com.rggmiranda.telemetryApp.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class TelemetryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelemetryAppApplication.class, args);
    }

    private DeviceService deviceService;
    private TelemetryService telemetryService;
    private SensorTypeService sensorTypeService;
    private JdbcUserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Bean
    public CommandLineRunner commandLineRunner(PasswordEncoder thePasswordEncoder, JdbcUserDetailsManager theUserDetailsManager, ApplicationContext ctx, SensorTypeService theSensorTypeService, TelemetryService thetelemetryService, DeviceService theDeviceService) {
        deviceService = theDeviceService;
        telemetryService = thetelemetryService;
        sensorTypeService = theSensorTypeService;
        userDetailsManager = theUserDetailsManager;
        passwordEncoder = thePasswordEncoder;


        return args -> {
            System.out.println("RM LOGGGING ----------- ");

            List<String> groups = userDetailsManager.findAllGroups();
            Set<String> users = new HashSet<>();
            for (String group : groups){
                Set<String> usersInGroup = new HashSet<>(userDetailsManager.findUsersInGroup(group)) ;
                users.addAll(usersInGroup);
            }


//            System.out.println(userDetailsManager.findAllGroups());


//            UserDetails user = User.builder()
//                    .username("adminuser")
//                    .password(passwordEncoder.encode("test"))
//                    .roles("USER", "ADMIN").disabled(false).build();//
//            theUserDetailsManager.createUser(user);
//            UserDetails user2 = User.builder()
//                    .username("admin")
//                    .password(passwordEncoder.encode("test"))
//                    .roles("ADMIN").disabled(false).build();//
//            theUserDetailsManager.createUser(user2);
//
//            UserDetails user3 = User.builder()
//                    .username("user")
//                    .password(passwordEncoder.encode("test"))
//                    .roles("USER").disabled(false).build();//
//            theUserDetailsManager.createUser(user3);







//            SensorType sensorType = new SensorType("Temperature");
//            sensorTypeService.save(sensorType);
//            sensorType = new SensorType("Humidity");
//            sensorTypeService.save(sensorType);
//            sensorType = new SensorType("Pressure");
//            sensorTypeService.save(sensorType);

//
//            theUserDetailsManager.deleteUser("john");
//            SensorType sensorType1 = sensorTypeService.findById(1);
//            SensorType sensorType2 = sensorTypeService.findById(2);
//            SensorType sensorType3 = sensorTypeService.findById(3);
//
//
//            //CREATE NEW DEVICE ------------------------------------------
//            Device device = new Device("DeviceA");
//            Sensor sensor = new Sensor("sensorZ1",sensorType1);
//            Sensor sensor2 = new Sensor("sensorV1",sensorType2);
//            Sensor sensor3 = new Sensor("sensorB1",sensorType2);
//
//            device.addSensor(sensor);
//            device.addSensor(sensor2);
//            device.addSensor(sensor3);
//            device.setUserName("user");
//            System.out.println(deviceService.save(device));





//            //FIND -------------------------------------------------------
////            List<Device> devices = deviceService.findAll();
////            System.out.println(devices);
////
////            Device device = devices.get(0);
////            Sensor sensor = device.getSensors().get(0);
//
//
//            Instant currentInstant = Instant.now();
//
//            Telemetry telemetry = new Telemetry(currentInstant, sensor, 125);
//            System.out.println(telemetry);
//            telemetryService.saveTelemetryData(telemetry);
//
//            Instant oneDayBefore = currentInstant.minus(1, ChronoUnit.DAYS);
//            Instant oneDayAfter = currentInstant.plus(1, ChronoUnit.DAYS);
//
//            System.out.println(telemetryService.getTelemetryData(sensor, oneDayBefore, oneDayAfter));
//            System.out.println(telemetryService.getLastTelemetry(sensor));

        };
    }

}
