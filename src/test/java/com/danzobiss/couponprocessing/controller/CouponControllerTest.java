package com.danzobiss.couponprocessing.controller;

import com.danzobiss.couponprocessing.client.MockAPIClient;
import com.danzobiss.couponprocessing.dto.CouponDTO;
import com.danzobiss.couponprocessing.dto.MockProductDTO;
import com.danzobiss.couponprocessing.entity.Coupon;
import com.danzobiss.couponprocessing.exception.InvalidCouponException;
import com.danzobiss.couponprocessing.pubsub.CouponQueueSender;
import com.danzobiss.couponprocessing.repository.CouponRepository;
import com.danzobiss.couponprocessing.service.CouponService;
import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponControllerTest {

    @Mock
    private CouponRepository repository;

    @Mock
    private MockAPIClient client;

    @Mock
    private CouponQueueSender queueSender;

    @Mock
    private Queue queueCoupon;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CouponService service;

    private Coupon coupon;
    private CouponDTO couponDTO;
    private MockProductDTO mockProductDTO;
    private Validator validator;


    @BeforeEach
    public void setUpCoupon() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        coupon = new Coupon();

        couponDTO = new CouponDTO();
        couponDTO.setCode44("75142018464820341008386582266732651087527208");
        couponDTO.setPurchaseDate(LocalDate.of(2024, 8,26));
        couponDTO.setTotalValue(3.00);
        couponDTO.setCompanyDocument("48296726000140");
        couponDTO.setState("SP");

        CouponDTO.ProductDTO productDTO = new CouponDTO.ProductDTO();
        productDTO.setName("Caneta Bic Azul");
        productDTO.setEan("1234567890123");
        productDTO.setUnitaryPrice(1.00);
        productDTO.setQuantity(3);

        couponDTO.setProducts(List.of(productDTO));

        BeanUtils.copyProperties(couponDTO, coupon);

        mockProductDTO = new MockProductDTO();
        mockProductDTO.setEan("1234567890123");
        mockProductDTO.setName("Caneta BIC Azul");
        mockProductDTO.setMinPrice(0.99);
        mockProductDTO.setMaxPrice(1.49);
    }

    @Test
    public void testCreateValidCoupon(){
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertTrue(violations.isEmpty());

        when(client.getProductByEAN(any(String.class))).thenReturn(ResponseEntity.ok(mockProductDTO));

        when(repository.save(any(Coupon.class))).thenReturn(coupon);

        Coupon savedCoupon = service.createCoupon(couponDTO);

        assertNotNull(savedCoupon);

    }

    @Test
    public void testCreateCouponInvalidCode44NotNumericChar(){
        couponDTO.setCode44("75142018464820341008386582266732651087A27208");
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testCreateCouponInvalidCode44Not44NumericDigits(){
        couponDTO.setCode44("75142018464820341008386582266727208");
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testCreateCouponInvalidCNPJ(){
        couponDTO.setCompanyDocument("48296726000110");
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertFalse(violations.isEmpty());

    }

    @Test
    public void testCreateCouponInvalidTotalValue(){
        couponDTO.setTotalValue(4.0);
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertTrue(violations.isEmpty());

        when(client.getProductByEAN(any(String.class))).thenReturn(ResponseEntity.ok(mockProductDTO));

        assertThrows(InvalidCouponException.class, () -> service.createCoupon(couponDTO));

    }

    @Test
    public void testCreateCouponInvalidProductEan(){
        couponDTO.getProducts().get(0).setEan("1234567890124");
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertTrue(violations.isEmpty());

        when(client.getProductByEAN(any(String.class))).thenThrow(FeignException.class);

        assertThrows(InvalidCouponException.class, () -> service.createCoupon(couponDTO));

    }

    @Test
    public void testCreateCouponInvalidProductValue(){
        couponDTO.getProducts().get(0).setUnitaryPrice(0.50);
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(couponDTO);
        assertTrue(violations.isEmpty());

        when(client.getProductByEAN(any(String.class))).thenReturn(ResponseEntity.ok(mockProductDTO));

        assertThrows(InvalidCouponException.class, () -> service.createCoupon(couponDTO));

    }

}
