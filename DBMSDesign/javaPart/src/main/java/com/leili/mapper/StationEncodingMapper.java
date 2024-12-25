package com.leili.mapper;

import com.leili.pojo.StationEncoding;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author leili
 * @description 针对表【station_encoding】的数据库操作Mapper
 * @createDate 2024-01-09 22:58:48
 * @Entity com.leili.pojo.StationEncoding
 */
@Mapper
public interface StationEncodingMapper {

    int deleteByPrimaryKey(StationEncoding record);

    int insert(StationEncoding record);

    int insertSelective(StationEncoding record);

    StationEncoding selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StationEncoding record);

    int updateByPrimaryKey(StationEncoding record);

    List<StationEncoding> selectAll();

}
