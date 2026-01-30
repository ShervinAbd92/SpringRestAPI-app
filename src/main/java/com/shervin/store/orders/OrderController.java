package com.shervin.store.orders;
import com.shervin.store.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController{

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> GetAllOrders(){
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDto GetOrderById(@PathVariable Long orderId)
    {
        return orderService.GetOrderById(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> HandleOrderNoTFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> HandleAccessDenialException(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
    }

}


