package com.leili.mapper;

import com.leili.pojo.StationViewAll;
import com.leili.pojo.StationViewAllSelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author leili
* @description 针对表【station_view_all】的数据库操作Mapper
* @createDate 2024-01-10 16:35:32
* @Entity com.leili.pojo.StationViewAll
*/
@Mapper
public interface StationViewAllMapper {

    int deleteByPrimaryKey(String string);

    int insert(StationViewAll record);

    int updateByPrimaryKey(StationViewAll record);

    List<StationViewAll> select(StationViewAllSelectOption stationViewAllSelectOption);

    int countAllWithCondition(StationViewAllSelectOption stationViewAllSelectOption);


}
