package com.shervin.store.orders;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for {@link OrderItem}
 */
@Data
public class OrderItemDto {

    private OrderProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}