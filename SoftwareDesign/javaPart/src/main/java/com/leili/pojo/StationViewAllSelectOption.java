package com.leili.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class StationViewAllSelectOption {
    private Integer page;

    private Integer itemsPerPage;

    private Integer offset;

    private StationViewAll searchBy;

    private Map<String, String> sortBy;
}
