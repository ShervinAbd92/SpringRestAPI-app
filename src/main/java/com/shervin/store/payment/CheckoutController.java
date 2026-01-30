package com.shervin.store.payment;
import com.shervin.store.common.ErrorDto;
import com.shervin.store.carts.CartIsEmptyException;
import com.shervin.store.carts.CartNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponseDto checkout(
            @Valid @RequestBody CheckoutRequestDto request){
            return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String,String> header,
            @RequestBody String payload
    ){
        checkoutService.HandleWebhookEvent(new WebhookRequest(header, payload));
    }

    // in case creating a checkoutsession throw paymentexcption error we are gonna catch it here
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> PaymentExceptionHandler(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error Creating Checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartIsEmptyException.class})
    public ResponseEntity<ErrorDto> HandleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
