package com.leili.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leili.bean.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leili.bean.vo.PortalVo;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
* @author leili
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2023-11-27 20:34:40
* @Entity com.leili.bean.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    IPage<Map<String,Object>> selectPageMap(IPage<Map<String, Object>> page, PortalVo portalVo);

    Map<String, Object> selectDetailByOne(Integer hid);
}




