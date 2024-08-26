package com.danzobiss.couponprocessing.exception;

import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCouponException extends RuntimeException {
    List<String> errorMessages;

    public InvalidCouponException(BindingResult bindingResult) {
        super("The coupon has inconsistencies in the registration");
        errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    public InvalidCouponException(String error) {
        super("The coupon has inconsistencies in the registration");
        errorMessages = List.of(error);
    }
}
