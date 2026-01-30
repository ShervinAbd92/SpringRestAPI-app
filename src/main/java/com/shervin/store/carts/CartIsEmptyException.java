package com.shervin.store.carts;

public class CartIsEmptyException extends RuntimeException {
    public CartIsEmptyException() {
        super("Cart is empty");
    }
}
