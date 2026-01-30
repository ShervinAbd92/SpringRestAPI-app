package com.shervin.store.carts;
import com.shervin.store.products.ProductNotFoundException;
import com.shervin.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartMapper cartMapper;

    public CartDto createCart(){

        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addItemToCart(UUID cartId, Long productId){

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart ==  null) {
            throw new CartNotFoundException();
        }
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        //go to cart and see if the product exists in it
        var cartItem = cart.AddItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto GetCart(UUID  cartId){

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto UpdateItem(UUID cartId,Long productId, Integer quantity){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
           throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return  cartMapper.toDto(cartItem);
    }

    public void DeleteItem(UUID cartId,Long productId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void ClearCart(UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }


}
