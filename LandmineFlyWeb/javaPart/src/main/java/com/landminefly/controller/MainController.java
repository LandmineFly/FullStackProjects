package com.landminefly.controller;

import com.landminefly.service.MainService;
import com.landminefly.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("main")
@CrossOrigin
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/weather")
    public Result<String> getWeather(@RequestParam(value = "district_id") String districtId,
                                     @RequestParam(value = "data_type") String dataType) {
        try {
            return Result.ok(mainService.getWeather(districtId, dataType));
        } catch (Exception e) {
            System.out.println("[ERROR]: Failed to get weather message");
            return Result.error("Failed to get weather message", null, false);
        }
    }
}
