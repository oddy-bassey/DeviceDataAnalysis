package com.revoltcode.DeviceDataAnalysis.controller;

import com.revoltcode.DeviceDataAnalysis.model.device.Device;
import com.revoltcode.DeviceDataAnalysis.service.DeviceService;
import com.revoltcode.DeviceDataAnalysis.service.DeviceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DeviceController {

    private final DeviceServiceImpl service;
    private final DeviceService deviceService;
    private boolean isStart = false;

    @RequestMapping("/generateData/{isStart}")
    public String startDeviceDataGeneration(@PathVariable boolean isStart){

        if(isStart){
            log.info("Initializing preset data...");
            deviceService.initializePresetData();
        }
        this.isStart = isStart;

        return "request received";
    }

        @Scheduled(fixedDelay = 10000)
    public void generateTask() {
        log.info("executing task...");
        if(this.isStart) {
            log.info("generating data...");
            deviceService.generateDeviceDataSet();
        }
    }

    /*@PostMapping("/save")
    public void savePerson(@RequestBody final Device device){
        //service.save(device);
    }

    @GetMapping("/find/{id}")
    public Device findById(@PathVariable final String id){
        return service.findById(id);
    }*/
}
