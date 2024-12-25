package com.leili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leili.bean.Headline;
import com.leili.bean.vo.PortalVo;
import com.leili.service.HeadlineService;
import com.leili.mapper.HeadlineMapper;
import com.leili.utils.JwtUtil;
import com.leili.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leili
 * @description 针对表【news_headline】的数据库操作Service实现
 * @createDate 2023-11-27 20:34:40
 */
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
        implements HeadlineService {

    @Autowired
    private HeadlineMapper headlineMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result<Map<String, Object>> findNewsPage(PortalVo portalVo) {
        IPage<Map<String, Object>> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        headlineMapper.selectPageMap(page, portalVo);

        HashMap<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageData", page.getRecords());
        pageInfo.put("pageNum", page.getCurrent());
        pageInfo.put("pageSize", page.getSize());
        pageInfo.put("totalPage", page.getPages());
        pageInfo.put("totalSize", page.getTotal());

        HashMap<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("pageInfo", pageInfo);

        return Result.ok(pageInfoMap);
    }

    @Override
    public Result<Map<String, Object>> showHeadlineDetail(Integer hid) {
        Map<String, Object> headline = headlineMapper.selectDetailByOne(hid);

        Headline updateViews = new Headline();
        updateViews.setHid((Integer) headline.get("hid"));
        updateViews.setVersion((Integer) headline.get("version"));
        updateViews.setPageViews((Integer) headline.get("pageViews") + 1);
        headlineMapper.updateById(updateViews);

        Map<String, Object> map = new HashMap<>();
        map.put("headline", headline);
        return Result.ok(map);
    }

    @Override
    public Result<Object> publish(Headline headline, String token) {
        Integer uid = jwtUtil.getUserId(token).intValue();
        headline.setPublisher(uid);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headline.setPageViews(0);

        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    @Override
    public Result<Map<String, Object>> findHeadlineByHid(String hid) {
        LambdaQueryWrapper<Headline> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Headline::getHid, hid).select(Headline::getHid, Headline::getTitle, Headline::getArticle, Headline::getType);
        List<Map<String, Object>> headlineMaps = headlineMapper.selectMaps(wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("headline", headlineMaps.get(0));
        return Result.ok(map);
    }

    @Override
    public Result<Object> updateData(Headline headline) {
        Integer hid = headline.getHid();
        Integer version = headlineMapper.selectById(hid).getVersion();

        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);
        return Result.ok(null);
    }

    @Override
    public Result<Object> removeByHid(String hid) {
        headlineMapper.deleteById(hid);
        return Result.ok(null);
    }
}




