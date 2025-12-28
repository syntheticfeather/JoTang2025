package com.example.demo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FilterRequest {

    String type;
    Double minPrice;
    Double maxPrice;
    Integer hours;
    @Pattern(regexp = "^(未售出|已售出)$", message = "status 只能是 '未售出' 、'已售出'")
    String status;

    @Pattern(regexp = "^(price|publish_time)$", message = "sortField 只能是 'price' 、'publish_time'")
    String sortField;
    @Pattern(regexp = "^(asc|desc)$", message = "sortDirection 只能是 'asc' 、'desc'")
    String sortDirection = "asc";

}
