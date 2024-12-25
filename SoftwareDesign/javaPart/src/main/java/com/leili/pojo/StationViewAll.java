package com.leili.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName station_view_all
 */
@Data
public class StationViewAll implements Serializable {
    private String 编码;

    private String 站名;

    private String 管理机构;

    private BigDecimal 东经;

    private BigDecimal 北纬;

    private String 流域水系;

    private String 河流名称;

    private String 基础水文;

    private String 降水蒸发;

    private String 实时雨水情;

    private String 水质;

    private static final long serialVersionUID = 1L;
}