package com.shervin.store.orders;
import com.shervin.store.auth.AuthServices;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthServices authServices;

    public List<OrderDto> getOrders() {

        var customer  = authServices.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(customer);
        return orders.stream().map(orderMapper::toDto).toList();
    }
    public OrderDto GetOrderById(Long orderId){
        var customer  = authServices.getCurrentUser();
        var order = orderRepository.getOrderById(orderId).
                orElseThrow(OrderNotFoundException::new);

        if (!order.isPlacedBy(customer)) {
            throw new AccessDeniedException("Access denied");
        }
        return  orderMapper.toDto(order);
    }
}
