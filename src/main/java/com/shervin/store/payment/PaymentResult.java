package com.shervin.store.payment;

import com.shervin.store.orders.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResult {

    private Long orderId;
    private PaymentStatus paymentStatus;
}
