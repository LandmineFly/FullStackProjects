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

    @PostMapping("/insert")
    public Result<Integer> insert(@RequestBody StationViewAll stationViewAll) {
        try {
            int insert = mapper.insert(stationViewAll);
            return Result.ok(insert, 0);
        } catch (Exception e) {
            return Result.error("添加失败", null, false);
        }
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

    @PutMapping("update")
    public Result<Integer> update(@RequestBody StationViewAll stationViewAll) {
        try {
            int update = mapper.updateByPrimaryKey(stationViewAll);
            return Result.ok(update, 0);
        } catch (Exception e) {
            return Result.error("更新失败", null, false);
        }
    }

    @DeleteMapping("delete")
    public Result<Integer> delete(@RequestBody StationViewAll stationViewAll) {
        try {
            int delete = mapper.deleteByPrimaryKey(stationViewAll.get编码());
            return Result.ok(delete, 0);
        } catch (Exception e) {
            return Result.error("删除失败", null, false);
        }
    }
}
