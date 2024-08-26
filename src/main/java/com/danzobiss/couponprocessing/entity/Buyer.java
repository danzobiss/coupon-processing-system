package com.danzobiss.couponprocessing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyerId;

    private String name;
    private LocalDate birthdate;
    private String document;

}
