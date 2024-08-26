package com.danzobiss.couponprocessing.client;

import com.danzobiss.couponprocessing.dto.MockProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mockAPIClient", url = "https://66ca2bd359f4350f064e9334.mockapi.io/api/v1")
public interface MockAPIClient {

    @GetMapping("/products/{ean}")
    ResponseEntity<MockProductDTO> getProductByEAN(@PathVariable("ean") String ean);
}
