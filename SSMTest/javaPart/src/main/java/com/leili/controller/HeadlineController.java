package com.leili.controller;

import com.leili.bean.Headline;
import com.leili.service.HeadlineService;
import com.leili.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {

    @Autowired
    private HeadlineService headlineService;

    @PostMapping("publish")
    public Result<Object> publish(@RequestBody Headline headline, @RequestHeader String token) {
        return headlineService.publish(headline, token);
    }

    @PostMapping("findHeadlineByHid")
    public Result<Map<String, Object>> findHeadlineByHid(String hid) {
        return headlineService.findHeadlineByHid(hid);
    }

    @PostMapping("update")
    public Result<Object> updateData(@RequestBody Headline headline) {
        return headlineService.updateData(headline);
    }

    @PostMapping("removeByHid")
    public Result<Object> removeByHid(String hid) {
        return headlineService.removeByHid(hid);
    }
}
