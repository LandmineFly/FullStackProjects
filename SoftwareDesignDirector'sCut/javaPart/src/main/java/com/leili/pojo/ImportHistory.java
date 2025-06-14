package com.leili.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ImportHistory {

    private String id;

    private String importType;

    private String importUrl;

    private Date importTime;

    private int importCount;

}
