package com.leili.service;

import com.leili.bean.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leili.utils.Result;

import java.util.List;
import java.util.Map;

/**
* @author leili
* @description 针对表【news_type】的数据库操作Service
* @createDate 2023-11-27 20:34:40
*/
public interface TypeService extends IService<Type> {

    /**
     * 返回所有新闻类型
     */
    Result<List<Type>> findAllTypes();
}
