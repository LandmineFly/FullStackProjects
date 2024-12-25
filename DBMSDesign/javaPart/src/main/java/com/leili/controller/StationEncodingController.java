package com.leili.controller;

import com.leili.mapper.StationEncodingMapper;
import com.leili.pojo.StationEncoding;
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

    @PostMapping("/insert")
    public Result<Integer> insert(@RequestBody StationEncoding stationEncoding) {
        try {
            int insert = mapper.insert(stationEncoding);
            return Result.ok(insert);
        } catch (Exception e) {
            return Result.error(e.getMessage(), null, true);
        }
    }

    @GetMapping("/select")
    public Result<List<StationEncoding>> selectAll() {
        try {
            List<StationEncoding> list = mapper.selectAll();
            return Result.ok(list);

        } catch (Exception e) {
            return Result.error("查询失败", null, false);
        }
    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody StationEncoding stationEncoding) {
        try {
            int update = mapper.updateByPrimaryKey(stationEncoding);
            return Result.ok(update);
        } catch (Exception e) {
            return Result.error("更新失败", null, false);
        }
    }

    @DeleteMapping("/delete")
    public Result<Integer> delete(@RequestBody StationEncoding stationEncoding) {
        try {
            int delete = mapper.deleteByPrimaryKey(stationEncoding);
            return Result.ok(delete);
        } catch (Exception e) {
            return Result.error(e.getMessage(), null, true);
        }
    }
}
