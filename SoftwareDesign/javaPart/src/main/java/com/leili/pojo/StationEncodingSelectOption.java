package com.leili.pojo;
import lombok.Data;

import java.util.Map;

@Data
public class StationEncodingSelectOption {
    private Integer page;

    private Integer itemsPerPage;

    private Integer offset;

    private StationEncoding searchBy;

    private Map<String, String> sortBy;
}
