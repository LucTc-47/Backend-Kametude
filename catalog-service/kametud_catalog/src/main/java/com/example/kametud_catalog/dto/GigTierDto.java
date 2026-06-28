package com.example.kametud_catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GigTierDto {

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer deliveryDays;
}
