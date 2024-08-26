package com.danzobiss.couponprocessing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyerDataDTO implements Serializable {

    private Long couponId;
    private String buyerName;
    private LocalDate buyerBirthDate;
    private String buyerDocument;

}
