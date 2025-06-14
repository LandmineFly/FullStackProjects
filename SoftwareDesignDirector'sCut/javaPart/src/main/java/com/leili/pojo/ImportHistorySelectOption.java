package com.leili.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class ImportHistorySelectOption {
    private Integer page;

    private Integer itemsPerPage;

    private Integer offset;

    // ImportHistory 就不做条件查询了，因为涉及到时间区间查询，有点麻烦，摆了
    // private ImportHistory searchBy;

    private Map<String, String> sortBy;
}
