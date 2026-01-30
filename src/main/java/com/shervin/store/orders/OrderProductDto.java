package com.shervin.store.orders;

import com.shervin.store.products.Product;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
@Data
public class OrderProductDto implements Serializable {

    private Long id;
    private String name;
    private BigDecimal price;
}