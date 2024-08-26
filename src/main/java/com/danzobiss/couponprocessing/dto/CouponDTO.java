package com.danzobiss.couponprocessing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;
import java.util.List;

@Data
public class CouponDTO {

    @Pattern(regexp = "\\d{44}", message = "The code44 must contain exactly 44 numeric digits.")
    private String code44;

    @PastOrPresent
    private LocalDate purchaseDate;

    @NotNull(message = "Total value is required.")
    @DecimalMin(value = "0.01", message = "The total value must be at least 1 cent")
    private Double totalValue;

    @NotNull(message = "The company document is required.")
    @CNPJ(message = "The company document is not a valid CNPJ.")
    private String companyDocument;

    @NotBlank(message = "The state is required.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "The state must have exactly 2 capital letters.")
    private String state;

    @NotEmpty(message = "The product list cannot be empty.")
    private List<@Valid ProductDTO> products;

    @Data
    public static class ProductDTO {

        @NotBlank(message = "Product name is required.")
        private String name;

        @NotNull(message = "EAN is required.")
        @Pattern(regexp = "\\d{13}", message = "The ean must contain exactly 13 numeric digits.")
        private String ean;

        @NotNull(message = "Unitary price is required.")
        @DecimalMin(value = "0.01", message = "The unitary price must be at least 1 cent")
        private Double unitaryPrice;

        @NotNull(message = "The quantity is required")
        @Min(value = 1, message = "The quantity must be greater than or equal to 1")
        private Integer quantity;
    }

}
