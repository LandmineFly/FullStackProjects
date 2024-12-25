package com.leili;

import com.leili.bean.Headline;
import com.leili.mapper.HeadlineMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MybatisTest {
    @Autowired
    public HeadlineMapper headlineMapper;

    @Test
    public void test1(){
        List<Headline> headlines = headlineMapper.selectList(null);
        System.out.println(headlines);
    }
}
