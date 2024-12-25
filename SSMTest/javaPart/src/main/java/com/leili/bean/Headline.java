package com.leili.bean;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

// 无需配置@TableName，因为已在application.yaml中进行了全局配置
@Data
public class Headline implements Serializable {
    // 就算已经在application.yaml中对@TableId进行了全局配置，此处也要加上@TableId注解
    // 否则无法使用mybatis-plus提供的xxById方法
    // class com.leili.bean.Headline ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method
    @TableId
    private Integer hid;

    private String title;

    private String article;

    private Integer type;

    private Integer publisher;

    private Integer pageViews;

    private Date createTime;

    private Date updateTime;

    // 配置乐观锁，指定版本字段
    @Version
    private Integer version;

    // 无需配置@TableLogic，因为已在application.yaml中进行了全局配置
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}