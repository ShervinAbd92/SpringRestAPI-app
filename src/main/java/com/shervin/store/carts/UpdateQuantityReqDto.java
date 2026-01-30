package com.shervin.store.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuantityReqDto {

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "quantity should be greater than zero")
    @Max(value=100, message = "quantity should be less than 100")
    private Integer  quantity;


}
