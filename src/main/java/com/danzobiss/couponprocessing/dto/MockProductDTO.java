package com.danzobiss.couponprocessing.dto;

import lombok.Data;

@Data
public class MockProductDTO {

    private String ean;
    private String name;
    private Double minPrice;
    private Double maxPrice;

}
