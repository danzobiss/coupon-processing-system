package com.danzobiss.couponprocessing.controller;

import com.danzobiss.couponprocessing.dto.CouponDTO;
import com.danzobiss.couponprocessing.entity.Coupon;
import com.danzobiss.couponprocessing.exception.InvalidCouponException;
import com.danzobiss.couponprocessing.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService service;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid CouponDTO dto, BindingResult bindingResult) {
        checkInvalidCoupon(bindingResult);

        Coupon createdEntity = service.createCoupon(dto);
        return status(CREATED).body(createdEntity);
    }

    private void checkInvalidCoupon(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidCouponException(bindingResult);
        }
    }

}
