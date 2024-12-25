package com.leili.controller;

import com.leili.mapper.StationEncodingMapper;
import com.leili.mapper.StationViewAllMapper;
import com.leili.pojo.StationEncoding;
import com.leili.pojo.StationViewAll;
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

    @PostMapping("/insert")
    public Result<Integer> insert(@RequestBody StationViewAll stationViewAll) {
        try {
            int insert = mapper.insert(stationViewAll);
            return Result.ok(insert);
        } catch (Exception e) {
            return Result.error("添加失败", null, false);
        }
    }

    @GetMapping("/select")
    public Result<List<StationViewAll>> selectAll() {
        try {
            List<StationViewAll> list = mapper.selectAll();
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("查询失败", null, false);
        }
    }

    @PutMapping("update")
    public Result<Integer> update(@RequestBody StationViewAll stationViewAll) {
        try {
            int update = mapper.updateByPrimaryKey(stationViewAll);
            return Result.ok(update);
        } catch (Exception e) {
            return Result.error("更新失败", null, false);
        }
    }

    @DeleteMapping("delete")
    public Result<Integer> delete(@RequestBody StationViewAll stationViewAll) {
        try {
            int delete = mapper.deleteByPrimaryKey(stationViewAll.get编码());
            return Result.ok(delete);
        } catch (Exception e) {
            return Result.error("删除失败", null, false);
        }
    }
}
