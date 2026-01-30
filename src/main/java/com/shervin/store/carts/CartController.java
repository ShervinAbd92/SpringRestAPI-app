package com.shervin.store.carts;
import com.shervin.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name="Carts")
public class CartController {

    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {

        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts").build().toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("{cartId}/items")
    @Operation(summary = "adda product to the cart")
    public CartItemDto AddToCart(
            @Parameter(description = "the ID of the cart")
            @PathVariable UUID cartId,
            @Valid  @RequestBody AddItemToProductReqDto request) {
            return cartService.addItemToCart(cartId, request.getProductId());
    }
    @GetMapping("{cartId}")
    public CartDto GetACart(
            @PathVariable UUID cartId) {
            return cartService.GetCart(cartId);
        }
    @PutMapping("{cartId}/items/{productId}")
    public CartItemDto UpdateCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateQuantityReqDto request
            ) {
        return cartService.UpdateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("{cartId}/items/{productId}")
    public ResponseEntity<?> DeleteCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        cartService.DeleteItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<?> ClearCart(
            @PathVariable("cartId") UUID cartId){
        cartService.ClearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> HandleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> HandleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error","Product was no found in the cart")
        );
    }
}


