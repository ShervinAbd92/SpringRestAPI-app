package com.shervin.store.orders;
import com.shervin.store.carts.Cart;
import com.shervin.store.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING) //tell hibernate to store it as stirng
    private PaymentStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // CascadeType.Remove is saying to hibernate if the order gets deleted go ahead an delete its items in orderItem table
    // OrphanRemoval= True is saying if an item from the order.item collection gets removed also remove it from the orderItem
    // or u can choose the option "ON DELETE CASCADE" when you are defining foreign keys in your table schema
    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true, fetch=FetchType.EAGER)
    private Set<OrderItem> items = new LinkedHashSet<>();

    
    public static Order CreateOrderFromCart(Cart cart, User customer){

        var order = new Order();
        order.setCustomer(customer);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        cart.getItems().forEach(cartItem -> {
            var orderItem= new OrderItem(order,cartItem.getProduct(), cartItem.getQuantity());
            order.items.add(orderItem);
        });
        return order;

    }
    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }
}