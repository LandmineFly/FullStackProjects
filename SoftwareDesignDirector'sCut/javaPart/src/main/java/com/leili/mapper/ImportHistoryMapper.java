package com.leili.mapper;

import com.leili.pojo.ImportHistory;
import com.leili.pojo.ImportHistorySelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author leili
 * @description 针对表【import_history】的数据库操作Mapper
 * @createDate 2025-02-21
 * @Entity com.leili.pojo.ImportHistory
 */
@Mapper
public interface ImportHistoryMapper {
    int insert(ImportHistory record);

    List<ImportHistory> select(ImportHistorySelectOption importHistorySelectOption);

    int countAllWithCondition(ImportHistorySelectOption importHistorySelectOption);

}
