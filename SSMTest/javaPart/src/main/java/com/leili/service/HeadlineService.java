package com.leili.service;

import com.leili.bean.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leili.bean.vo.PortalVo;
import com.leili.utils.Result;

import java.util.Map;

/**
* @author leili
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2023-11-27 20:34:40
*/
public interface HeadlineService extends IService<Headline> {

    /**
     * 根据条件查询新闻标题
     */
    Result<Map<String, Object>> findNewsPage(PortalVo portalVo);

    /**
     * 根据id显示新闻详情
     */
    Result<Map<String, Object>> showHeadlineDetail(Integer hid);

    /**
     * 发布新闻
     */
    Result<Object> publish(Headline headline, String token);

    /**
     * 修改新闻时的回显
     */
    Result<Map<String, Object>> findHeadlineByHid(String hid);

    /**
     * 修改新闻
     */
    Result<Object> updateData(Headline headline);

    /**
     * 删除新闻
     */
    Result<Object> removeByHid(String hid);
}
