package com.leili.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName station_encoding
 */
@Data
public class StationEncoding implements Serializable {
    private String iid;

    private String sttype;

    private String iname;

    private String pyk;

    private String stid;

    private String nt;

    // 导入时使用，用于记录导入时未满足条件的原因
    private List<String> NotSatisfiedReason;

    private static final long serialVersionUID = 1L;
}