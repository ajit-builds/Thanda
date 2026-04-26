package com.thanda.ecommerce.service;

import com.thanda.ecommerce.entity.CartItem;
import com.thanda.ecommerce.entity.Product;
import com.thanda.ecommerce.repository.CartItemRepository;
import com.thanda.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(Long userId, Long productId, Integer qty) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (product.getStock() < qty)
            throw new RuntimeException("Insufficient stock");

        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElse(new CartItem(userId, product));
        item.setQuantity(item.getQuantity() == null ? qty : item.getQuantity() + qty);
        return cartRepository.save(item);
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}
