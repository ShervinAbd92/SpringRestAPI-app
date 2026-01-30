package com.shervin.store.products;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChangePriceReqDto {

    private BigDecimal oldPrice;
    private BigDecimal newPrice;
}
