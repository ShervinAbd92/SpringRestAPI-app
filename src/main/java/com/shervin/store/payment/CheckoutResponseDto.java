package com.shervin.store.payment;

import lombok.Data;

@Data
public class CheckoutResponseDto {

    private Long orderId;
    private String checkoutUrl;

    public CheckoutResponseDto(Long orderId,String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
        this.orderId = orderId;
    }
}
