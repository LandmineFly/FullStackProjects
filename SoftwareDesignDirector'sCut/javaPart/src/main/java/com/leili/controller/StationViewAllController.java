package com.leili.controller;

import com.leili.mapper.StationViewAllMapper;
import com.leili.pojo.StationViewAll;
import com.leili.pojo.StationViewAllSelectOption;
import com.leili.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sv")
@CrossOrigin
public class StationViewAllController {
    @Autowired
    StationViewAllMapper mapper;

    private Integer countAllWithCondition(@RequestBody StationViewAllSelectOption stationViewAllSelectOption) {
        return mapper.countAllWithCondition(stationViewAllSelectOption);
    }

    @PostMapping("/select")
    public Result<List<StationViewAll>> select(@RequestBody StationViewAllSelectOption stationViewAllSelectOption) {
        try {
            stationViewAllSelectOption.setOffset(stationViewAllSelectOption.getItemsPerPage() * (stationViewAllSelectOption.getPage() - 1));
            List<StationViewAll> list = mapper.select(stationViewAllSelectOption);
            int totalCount = countAllWithCondition(stationViewAllSelectOption);
            return Result.ok(list, totalCount);
        } catch (Exception e) {
            return Result.error("查询失败", null, false);
        }
    }
}
