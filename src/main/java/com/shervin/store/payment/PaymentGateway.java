package com.shervin.store.payment;


import com.shervin.store.orders.Order;

import java.util.Optional;

public interface PaymentGateway {

    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);

}
