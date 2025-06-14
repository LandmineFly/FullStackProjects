package com.leili.controller;

import com.leili.mapper.StationEncodingMapper;
import com.leili.pojo.StationEncoding;
import com.leili.pojo.StationEncodingSelectOption;
import com.leili.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("se")
@CrossOrigin
public class StationEncodingController {
    @Autowired
    StationEncodingMapper mapper;

    private Integer countAllWithCondition(StationEncodingSelectOption stationEncodingSelectOption) {
        return mapper.countAllWithCondition(stationEncodingSelectOption);
    }

    @PostMapping("/insert")
    public Result<Integer> insert(@RequestBody StationEncoding stationEncoding) {
        try {
            int insert = mapper.insert(stationEncoding);
            return Result.ok(insert, 0);
        } catch (Exception e) {
            return Result.error(e.getMessage(), null, true);
        }
    }

    @PostMapping("/select")
    public Result<List<StationEncoding>> select(@RequestBody StationEncodingSelectOption stationEncodingSelectOption) {
        try {
            stationEncodingSelectOption.setOffset(stationEncodingSelectOption.getItemsPerPage() * (stationEncodingSelectOption.getPage() - 1));
            List<StationEncoding> list = mapper.select(stationEncodingSelectOption);
            int totalCount = countAllWithCondition(stationEncodingSelectOption);
            return Result.ok(list, totalCount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("查询失败", null, false);
        }
    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody StationEncoding stationEncoding) {
        try {
            int update = mapper.updateByPrimaryKey(stationEncoding);
            return Result.ok(update, 0);
        } catch (Exception e) {
            return Result.error("更新失败", null, false);
        }
    }

    @DeleteMapping("/delete")
    public Result<Integer> delete(@RequestBody StationEncoding stationEncoding) {
        try {
            int delete = mapper.deleteByPrimaryKey(stationEncoding);
            return Result.ok(delete, 0);
        } catch (Exception e) {
            return Result.error(e.getMessage(), null, true);
        }
    }
}
