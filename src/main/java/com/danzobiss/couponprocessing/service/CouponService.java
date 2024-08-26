package com.danzobiss.couponprocessing.service;

import com.danzobiss.couponprocessing.client.MockAPIClient;
import com.danzobiss.couponprocessing.dto.CouponDTO;
import com.danzobiss.couponprocessing.dto.MockProductDTO;
import com.danzobiss.couponprocessing.entity.Coupon;
import com.danzobiss.couponprocessing.exception.InvalidCouponException;
import com.danzobiss.couponprocessing.pubsub.CouponQueueSender;
import com.danzobiss.couponprocessing.repository.CouponRepository;
import com.danzobiss.couponprocessing.util.Util;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository repository;

    @Autowired
    private MockAPIClient mockAPIClient;

    @Autowired
    private CouponQueueSender queueSender;

    public Coupon createCoupon(@Validated CouponDTO couponDTO) {

        validateProducts(couponDTO.getProducts());
        validateCompanyDocument(couponDTO.getCompanyDocument());
        validateTotalValue(couponDTO);

        Coupon createdCoupon = repository.save(mapToEntity(couponDTO));

        queueSender.sendCoupon(createdCoupon);

        return createdCoupon;
    }

    private void validateProducts(List<CouponDTO.ProductDTO> products) {
        products.forEach(productDTO -> {
            MockProductDTO mockProduct = getProductByEan(productDTO.getEan());

            if (mockProduct == null) {
                throw new InvalidCouponException("The product with EAN " + productDTO.getEan() + " does not exist");
            }

            Double unitaryPrice = productDTO.getUnitaryPrice();
            if (unitaryPrice < mockProduct.getMinPrice() || unitaryPrice > mockProduct.getMaxPrice()) {
                throw new InvalidCouponException("The unitary price must be between " + mockProduct.getMinPrice() + " and " + mockProduct.getMaxPrice());
            }
        });
    }

    private MockProductDTO getProductByEan(String ean) {
        try {
            ResponseEntity<MockProductDTO> response = mockAPIClient.getProductByEAN(ean);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (FeignException ex) {
            // Log the exception if necessary
        }
        return null;
    }

    private void validateCompanyDocument(String companyDocument) {
        if (!Util.isValidCNPJ(companyDocument)) {
            throw new InvalidCouponException("The company document is not a valid CNPJ");
        }
    }

    private void validateTotalValue(CouponDTO couponDTO) {
        Double sum = couponDTO.getProducts().stream()
                .mapToDouble(product -> product.getQuantity() * product.getUnitaryPrice())
                .sum();

        if (!sum.equals(couponDTO.getTotalValue())) {
            throw new InvalidCouponException("The total value is not valid based on quantity and unitary price of all products");
        }
    }

    private Coupon mapToEntity(CouponDTO couponDTO) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(couponDTO, Coupon.class);
    }

}
