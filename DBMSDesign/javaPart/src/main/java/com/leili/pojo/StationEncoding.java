package com.leili.pojo;

import java.io.Serializable;

import lombok.Data;

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

    private static final long serialVersionUID = 1L;
}