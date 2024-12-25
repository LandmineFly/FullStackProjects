package com.leili.bean.vo;

import lombok.Data;

/**
 * Vo即ViewObject/ValueObject，表示用于接收前端传递参数的实体类
 */
@Data
public class PortalVo {
    private String keyWords;
    // 0 表示所有类别
    private int type = 0;
    private int pageNum = 1;
    private int pageSize = 10;
}
