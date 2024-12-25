package com.leili.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class UserSelectOption {
    private Integer page;

    private Integer itemsPerPage;

    private Integer offset;

    private User searchBy;

    private Map<String, String> sortBy;
}
