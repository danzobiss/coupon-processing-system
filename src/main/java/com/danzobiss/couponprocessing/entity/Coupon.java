package com.danzobiss.couponprocessing.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String code44;

    private LocalDate purchaseDate;

    private Double totalValue;

    private String companyDocument;

    private String state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coupon_id")
    private List<Product> products;

}
