package com.shervin.store.carts;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToProductReqDto {

    @NotNull
    private Long productId;
}
