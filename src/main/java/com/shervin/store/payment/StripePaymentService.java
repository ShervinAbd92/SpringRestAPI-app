package com.shervin.store.payment;

import com.shervin.store.orders.Order;
import com.shervin.store.orders.OrderItem;
import com.shervin.store.orders.PaymentStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentService implements PaymentGateway {

    @Value("${websiteURL}")
    private String websiteUrl;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {

        try{
            //Create Checkout Session
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId="+ order.getId()) //the URL that stripe will redirect the client if payment is succesful
                    .setCancelUrl(websiteUrl +"/checkout-cancel")
                    .setPaymentIntentData(createPaymentIntent(order)
                        );

            //add orderItems to this builder, by going through order's item and create LineItem
            order.getItems().forEach(item -> {
                var LineItem = CreateLineItem(item);
                builder.addLineItem(LineItem);
            });
            // finally, create SessionCreateParam which we then feed into Session
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getId());
        }
        catch (StripeException e){
            System.out.println(e.getMessage());
            throw new PaymentException(); //general purpose expeciotn
        }
    }

    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", order.getId().toString()).build();
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try{
            var payload = request.getPayload();
            var signature = request.getHeader().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecret);

            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                case "payment_intent.payment_failed" ->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

                default ->  Optional.empty();
            };
        }
        catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature");
        }
    }

    private Long extractOrderId(Event event){

        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Couldn't deserialize Stripe event. Check the SDK and API version"));

        var paymentIntent = (PaymentIntent) stripeObject;
        return  Long.valueOf(paymentIntent.getMetadata().get("order_id"));

    }
    private SessionCreateParams.LineItem CreateLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(CreatePriceData(item)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData CreatePriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName()
                ).build();
    }

}
