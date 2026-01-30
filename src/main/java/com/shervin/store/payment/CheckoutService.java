package com.shervin.store.payment;
import com.shervin.store.orders.Order;
import com.shervin.store.carts.CartIsEmptyException;
import com.shervin.store.carts.CartNotFoundException;
import com.shervin.store.carts.CartRepository;
import com.shervin.store.orders.OrderRepository;
import com.shervin.store.auth.AuthServices;
import com.shervin.store.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthServices authServices;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponseDto checkout(CheckoutRequestDto request){

        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.getItems().isEmpty()){
            throw new CartIsEmptyException();
        }

        var customer = authServices.getCurrentUser();
        var order = Order.CreateOrderFromCart(cart, customer);
        orderRepository.save(order);
        //processing payment with 3rd party (Stripe)
        try{
            var session = paymentGateway.createCheckoutSession(order);
            cartService.ClearCart(cart.getId());
            return new CheckoutResponseDto(order.getId(),session.getCheckoutURL());
        }
        catch(PaymentException e){
            orderRepository.delete(order);
            throw e;
        }
    }

    // construct event, deserialize stripObject, getting the orderId
    // updating order status
    // take WebhookRequest -> new object{ orderId, paymentStatus} (paymentResult)
    public void HandleWebhookEvent(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }

}
