package com.leili.controller;

import com.leili.mapper.ImportHistoryMapper;
import com.leili.mapper.StationEncodingMapper;
import com.leili.pojo.*;
import com.leili.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("import")
@CrossOrigin
public class DataImportController {

    @Autowired
    private ImportHistoryMapper importHistoryMapper;
    @Autowired
    private StationEncodingMapper stationEncodingMapper;

    private final String iidPattern = "^[0-9]{3}.[0-9]{3,4}$";
    private final List<String> sttypeLegalValue = Arrays.asList("基础水文", "降水蒸发", "实时雨水情", "水质");

    private Integer countAllWithCondition(ImportHistorySelectOption importHistorySelectOption) {
        return importHistoryMapper.countAllWithCondition(importHistorySelectOption);
    }

    private DataSource createDataSource(DatabaseConnectionInfo databaseConnectionInfo) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(databaseConnectionInfo.getUrl());
        dataSource.setUsername(databaseConnectionInfo.getUsername());
        dataSource.setPassword(databaseConnectionInfo.getPassword());
        return dataSource;
    }

    private boolean isAbleToConnect(DataSource dataSource) {
        // try-with-resources语句，Connection对象在使用完毕后会自动关闭，简化了代码逻辑
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 去除前后空格，取出后如果为空字符串，则将其置为null
     */
    private void setBlankToNull(StationEncoding se) {
        if (se.getIid() != null) {
            se.setIid(se.getIid().trim());
            if (Objects.equals(se.getIid(), "")) {
                se.setIid(null);
            }
        }
        if (se.getSttype() != null) {
            se.setSttype(se.getSttype().trim());
            if (Objects.equals(se.getSttype(), "")) {
                se.setSttype(null);
            }
        }
        if (se.getIname() != null) {
            se.setIname(se.getIname().trim());
            if (Objects.equals(se.getIname(), "")) {
                se.setIname(null);
            }
        }
        if (se.getPyk() != null) {
            se.setPyk(se.getPyk().trim());
            if (Objects.equals(se.getPyk(), "")) {
                se.setPyk(null);
            }
        }
        if (se.getStid() != null) {
            se.setStid(se.getStid().trim());
            if (Objects.equals(se.getStid(), "")) {
                se.setStid(null);
            }
        }
        if (se.getNt() != null) {
            se.setNt(se.getNt().trim());
            if (Objects.equals(se.getNt(), "")) {
                se.setNt(null);
            }
        }
    }

    private void checkDetail(StationEncoding se, List<String> reasons) {
        if (se.getIid() == null || se.getSttype() == null || se.getIname() == null || se.getStid() == null) {
            reasons.add("编码, 测站类型, 对象名, 测站号不能为空。");
        }
        if (se.getIid() != null && !Pattern.matches(iidPattern, se.getIid())) {
            reasons.add("编码不满足格式要求。");
        }
        if (se.getSttype() != null && !sttypeLegalValue.contains(se.getSttype())) {
            reasons.add("测站类型不满足格式要求。");
        }
        // 在确定没有其他问题后，再判断主键是否重复
        if (reasons.isEmpty() && stationEncodingMapper.isDuplicate(se) != 0) {
            reasons.add("主键重复(编码,测站类型)。");
        }
    }


    private StationEncoding check(Map<String, Object> map) {
        StationEncoding se = new StationEncoding();
        List<String> reasons = new ArrayList<>();

        se.setIid((String) map.get("IID"));
        se.setSttype((String) map.get("STTYPE"));
        se.setIname((String) map.get("INAME"));
        se.setPyk((String) map.get("PYK"));
        se.setStid((String) map.get("STID"));
        se.setNt((String) map.get("NT"));

        setBlankToNull(se);
        checkDetail(se, reasons);
        se.setNotSatisfiedReason(reasons);
        return se;
    }

    @PostMapping("/select")
    public Result<List<ImportHistory>> select(@RequestBody ImportHistorySelectOption importHistorySelectOption) {
        try {
            importHistorySelectOption.setOffset(importHistorySelectOption.getItemsPerPage() * (importHistorySelectOption.getPage() - 1));
            if (importHistorySelectOption.getSortBy() != null) {
                // 如果 sortBy 不为空（即有排序需求），则要对需要排序的字段名（即key）进行修改，将驼峰命名法转换为下划线命名法
                Map<String, String> sortBy = importHistorySelectOption.getSortBy();
                String key = sortBy.get("key");
                key = key.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
                sortBy.replace("key", key);
                importHistorySelectOption.setSortBy(sortBy);
            }
            List<ImportHistory> list = importHistoryMapper.select(importHistorySelectOption);
            int totalCount = countAllWithCondition(importHistorySelectOption);
            return Result.ok(list, totalCount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("查询失败", null, false);
        }
    }

    @PostMapping("/get-tables")
    public Result<List<String>> getTables(@RequestBody DatabaseConnectionInfo databaseConnectionInfo) {
        try {
            // 检查连接
            DataSource dataSource = createDataSource(databaseConnectionInfo);
            if (!isAbleToConnect(dataSource)) {
                return Result.error("连接失败", null, false);
            }
            ArrayList<String> tables = new ArrayList<>();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            /* queryForList返回的数据belike:
                [
                    {"Tables_in_project_1": "engineering"},
                    {"Tables_in_project_1": "manage_way"},
                ]
            */
            for (Map<String, Object> item : jdbcTemplate.queryForList("SHOW TABLES")) {
                item.forEach((key, value) -> {
                    tables.add(value.toString());
                });
            }
            return Result.ok(tables, null);
        } catch (Exception e) {
            return Result.error("连接失败", null, false);
        }
    }

    @PostMapping("/start-import")
    public Result<List<StationEncoding>> startImport(@RequestBody DatabaseConnectionInfo databaseConnectionInfo) {
        try {
            // 正则表达式验证表名格式
            String tableNamePattern = "^[a-zA-Z0-9_]+$"; // 仅允许字母、数字和下划线
            if (!Pattern.matches(tableNamePattern, databaseConnectionInfo.getTable())) {
                return Result.error("表名非法", null, false);
            }
            // 检查连接
            DataSource dataSource = createDataSource(databaseConnectionInfo);
            if (!isAbleToConnect(dataSource)) {
                return Result.error("连接失败(可能是地址错误)", null, false);
            }

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            // 为了实现前端多选全选的逻辑，这里要返回所有数据，不能使用分页查询
            String sql = "SELECT * FROM " + databaseConnectionInfo.getTable();
            String sqlCount = "SELECT COUNT(*) FROM " + databaseConnectionInfo.getTable();
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
            // 执行sqlCount返回的结果belike: [{"COUNT(*)": 100}]，注意数字是Long类型
            List<Map<String, Object>> count = jdbcTemplate.queryForList(sqlCount);

            List<StationEncoding> list = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                list.add(check(map));
            }

            // 当时定义Result的时候大意了，totalCount定义成了Integer，所以这里得转一下
            // 如果数据量过大，会抛出ArithmeticException
            return Result.ok(list, Math.toIntExact((Long) count.get(0).get("COUNT(*)")));
        } catch (DataAccessException e) {
            return Result.error("连接失败(可能是表名错误)", null, false);
        } catch (ArithmeticException e) {
            return Result.error("数据量过大", null, false);
        }
    }

    @PostMapping("/recheck")
    public Result<List<StationEncoding>> recheck(@RequestBody List<StationEncoding> stationEncodings) {
        try {
            for (StationEncoding se : stationEncodings) {
                ArrayList<String> reasons = new ArrayList<>();
                setBlankToNull(se);
                checkDetail(se, reasons);
                se.setNotSatisfiedReason(reasons);
            }
            return Result.ok(stationEncodings, null);
        } catch (Exception e) {
            return Result.error("校验失败", null, false);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/formal-import")
    public Result<Integer> formalImport(@RequestParam String importUrl, @RequestBody List<StationEncoding> stationEncodings) {
        try {
            for (StationEncoding se : stationEncodings) {
                setBlankToNull(se);
            }
            // 不用检查了，因为MySQL会检查的，数据非法就会SQL执行失败并抛出异常
            stationEncodingMapper.batchInsert(stationEncodings);
            ImportHistory importHistory = new ImportHistory();
            // 目前只支持MySQL导入，直接写死
            importHistory.setImportType("MySQL");
            importHistory.setImportUrl(importUrl);
            importHistory.setImportTime(new Date());
            importHistory.setImportCount(stationEncodings.size());
            importHistoryMapper.insert(importHistory);
            return Result.ok(stationEncodings.size(), null);
        } catch (Exception e) {
            // 只有在抛出异常后事务才会回滚。本来这个异常应该在Service层抛的，然后在Controller层捕获，再给前端返回响应
            // 但是最开始的时候偷懒，没有设Service层，所以只能在这里抛出
            // 这就有点尴尬了，抛了就不能返回响应，不抛就不会回滚
            // 因此只能曲线救国，在Controller类中定义一个方法，并使用 @ExceptionHandler 注解来处理特定类型的异常
            throw new IllegalArgumentException("illegal data");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<Integer>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(400)
                .body(Result.error(e.getMessage(), null, false));
    }
}
