package com.shervin.store.payment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
