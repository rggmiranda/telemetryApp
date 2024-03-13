package com.rggmiranda.telemetryApp.controller;

import com.rggmiranda.telemetryApp.model.Device;
import com.rggmiranda.telemetryApp.model.Sensor;
import com.rggmiranda.telemetryApp.model.SensorType;
import com.rggmiranda.telemetryApp.service.DeviceService;
import com.rggmiranda.telemetryApp.service.SensorTypeService;
import com.rggmiranda.telemetryApp.service.TelemetryService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gui")
public class WebController implements ErrorController {
    private DeviceService deviceService;
    private TelemetryService telemetryService;
    private JdbcUserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;
    private SensorTypeService sensorTypeService;


    @Autowired
    public WebController(SensorTypeService theSensorTypeService, PasswordEncoder thePasswordEncoder, DeviceService theDeviceService, TelemetryService theTelemetryService, JdbcUserDetailsManager theUserDetailsManager) {
        this.deviceService = theDeviceService;
        this.telemetryService = theTelemetryService;
        this.userDetailsManager = theUserDetailsManager;
        this.passwordEncoder = thePasswordEncoder;
        this.sensorTypeService = theSensorTypeService;
    }

    @GetMapping("/telemetries")
    public String telemetries(
            @RequestParam("sensorId") int sensorId,
            @RequestParam("deviceId") int deviceId,
            Model model) {
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("sensorId", sensorId);
        return "devices/telemetries";
    }



    @GetMapping({"/home",  "", "/", "/error"})
    public String homePage(Model model) {
        boolean authenticated = false;
        String userName = "";
        List<String> roles = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString() != "anonymousUser") {
            authenticated = true;
            userName = authentication.getName();
            roles = Arrays.asList(authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray(String[]::new));
        }
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("userName", userName);
        model.addAttribute("roles", roles);
        return "home";
    }

    @GetMapping("/devices/deviceList")
    public String devicesList(Model model) {
        List<Device> devices = deviceService.findAll();
        boolean authenticated = false;
        String userName = "";
        List<String> roles = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString() != "anonymousUser") {
            authenticated = true;
            userName = authentication.getName();
            roles = Arrays.asList(authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray(String[]::new));
        }
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("userName", userName);
        model.addAttribute("roles", roles);

        model.addAttribute("devices", devices);
        return "devices/device_list";
    }

    @GetMapping("/devices/newDevice")
    public String newDeviceForm(Model model) {
        Device device = new Device();
        model.addAttribute("device", device);

        return "devices/form_new_device";
    }

    @GetMapping("/devices/removeDevice")
    public String removeDevice(@RequestParam("deviceId") int id, Model model) {
        deviceService.deleteById(id);
        return "redirect:/gui/devices/deviceList";
    }

    @GetMapping("/devices/modifyDevice")
    public String modifyDevice(@RequestParam("deviceId") int id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        return "devices/form_new_device";
    }

    @PostMapping("/devices/processDeviceForm")
    public String processDevice(Model model,
                                @ModelAttribute("device") Device device,
                                BindingResult bindingResult) {
        createDevice(device);
        return "redirect:/gui/devices/deviceList";
    }

    public void createDevice(Device device){
        if (device.getId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getPrincipal().toString().equals("anonymousUser")) {
                device.setUserName(authentication.getName());
            }
        }
        deviceService.save(device);
    }

    @GetMapping("/devices/addSensor")
    public String addSensor(@RequestParam("deviceId") int deviceId, @RequestParam("sensorName") Optional<String> optionalSensorName, Model model) {
        Sensor sensor;
        Device device = deviceService.findById(deviceId);
        if(optionalSensorName.isPresent()){
            String sensorName = optionalSensorName.get();
            Set<Sensor> sensors = device.getSensors();
            sensor = sensors.stream()
                    .filter(theSensor -> theSensor.getName().equals(sensorName))
                    .findFirst()
                    .orElse(null);
        }else{
            sensor = new Sensor();
            device.addSensor(sensor);
        }
        List<SensorType> sensorTypes = sensorTypeService.findAll();
        model.addAttribute("sensor", sensor);
        model.addAttribute("sensorTypes", sensorTypes);
        return "devices/form_new_sensor";
    }



    @PostMapping("/devices/addSensor")
    public String addSensor(
            Model model,
            @ModelAttribute("sensor") Sensor sensor) {
        Device device = deviceService.findById(sensor.getDevice().getId());
        SensorType sensorType = sensorTypeService.findById(sensor.getSensorType().getId());
        sensor.setSensorType(sensorType);
        if (sensor.getId()==null){
            device.addSensor(sensor);
        }else{
            device.updateSensor(sensor);
        }

        deviceService.save(device);
        model.addAttribute("device", device);
        return "devices/form_new_device";
    }


    @GetMapping("/devices/removeSensor")
    public String removeSensor(@RequestParam("deviceId") int deviceId,
                               @RequestParam("sensorName") Optional<String> optionalSensorName,
                               Model model) {
        Sensor sensor;
        Device device = deviceService.findById(deviceId);
        if(optionalSensorName.isPresent()){
            String sensorName = optionalSensorName.get();
            Set<Sensor> sensors = device.getSensors();
            sensor = sensors.stream()
                    .filter(theSensor -> theSensor.getName().equals(sensorName))
                    .findFirst()
                    .orElse(null);
            device.removeSensor(sensor);
        }
        deviceService.save(device);
        model.addAttribute("device", device);
        return "devices/form_new_device";
    }





// USER SELF-SERVICE ----------------------------------------------------------------------------------

    @GetMapping("/login")
    public String customLogin() {
        return "users/login";
    }

    @GetMapping("/change_password")
    public String changePassword() {
        return "users/change_password";
    }

    @PostMapping("/change_password")
    public String processChangePassword(Model model,
                                        @ModelAttribute("oldPassword") String oldPassword,
                                        @ModelAttribute("password") String password) {
        String error = "";
        try {
            userDetailsManager.changePassword(oldPassword, passwordEncoder.encode(password));
            return "redirect:/gui/home";
        } catch (BadCredentialsException e) {
            error = e.getMessage();
            model.addAttribute("oldPasswordError", error);
        }
        return "users/change_password";
    }


// USERS -----------------------------------------------------------------------------------------------

    @GetMapping("/usersList")
    public String userList(Model model) {
        List<String> groups = userDetailsManager.findAllGroups();
        Set<UserDetails> users = new HashSet<>();
        Set<String> userNames = new HashSet<>();
        for (String group : groups){
            Set<String> usersInGroup = new HashSet<>(userDetailsManager.findUsersInGroup(group)) ;
            userNames.addAll(usersInGroup);
        }

        for (String user : userNames){
            users.add(userDetailsManager.loadUserByUsername(user));
        }

        String currentUser = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString() != "anonymousUser") {
            currentUser = authentication.getName();
        }

        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "users/user_list";
    }


    @NoArgsConstructor
    @Getter
    @Setter
    class UserForm {
        private String userName;
        private List<String> authorities;

    }

    @GetMapping({"/users/modifyUser","/users/newUser"})
    public String modifyUser(@RequestParam("userName") Optional<String> optionalUserName, Model model) {

        UserForm userForm = new UserForm();

        if(optionalUserName.isPresent()){
            UserDetails user = userDetailsManager.loadUserByUsername(optionalUserName.get());
            userForm.setUserName(optionalUserName.get());
            userForm.setAuthorities(user.getAuthorities()
                    .stream()
                    .map(Object::toString) // Optional: If authorities are not strings
                    .collect(Collectors.toList())
            );
        }
        List<String> availableAuthorities = userDetailsManager.findAllGroups();
        model.addAttribute("userForm", userForm);
        model.addAttribute("availableAuthorities", availableAuthorities);
        return "users/form_new_user";
    }


    @PostMapping("/users/processUserForm")
    public String processChangeUser(Model model, @ModelAttribute("userForm") UserForm userForm) {
        UserDetails userDetails;
        List<String> authorities = userForm.getAuthorities();

        authorities.removeIf(authority -> authority.trim().equals(""));
        for(String authority : authorities ){
            authorities.set(authorities.indexOf(authority),authority.replaceFirst("ROLE_",""));
        }
        String[] authoritiesArray = authorities.toArray(new String[0]);

        if(userDetailsManager.userExists(userForm.getUserName())){
            userDetails = userDetailsManager.loadUserByUsername(userForm.getUserName());
            userDetails = User.builder().username(userDetails.getUsername()).password(userDetails.getPassword()).roles(authoritiesArray).build();
            userDetailsManager.updateUser(userDetails);
        }else{
            userDetails = User.builder().username(userForm.getUserName()).password( passwordEncoder.encode("test123")).roles(authoritiesArray).build();
            userDetailsManager.createUser(userDetails);
        }
        return "redirect:/gui/usersList";
    }

    @GetMapping("/users/resetPassword")
    public String resetPassword(@RequestParam("userName") String userName, Model model) {
        if(userDetailsManager.userExists(userName)){
            UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);
            userDetails = User.builder().username(userDetails.getUsername()).password(passwordEncoder.encode("test123")).authorities(userDetails.getAuthorities()).build();
            userDetailsManager.updateUser(userDetails);
        }
        return "redirect:/gui/usersList";
    }

    @GetMapping("/users/removeUser")
    public String removeUser(@RequestParam("userName") String userName, Model model) {
        List<Device> userDevices = deviceService.findByUserName(userName);
        for(Device device : userDevices){
            deviceService.deleteById(device.getId());
        }
        userDetailsManager.deleteUser(userName);
        return "redirect:/gui/usersList";
    }
}



