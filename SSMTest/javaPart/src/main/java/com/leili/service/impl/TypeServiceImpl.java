package com.leili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leili.bean.Type;
import com.leili.service.TypeService;
import com.leili.mapper.TypeMapper;
import com.leili.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author leili
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2023-11-27 20:34:40
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public Result<List<Type>> findAllTypes() {
        List<Type> types = typeMapper.selectList(null);
        Map<String, Object> map = new HashMap<>();
        return Result.ok(types);
    }
}




