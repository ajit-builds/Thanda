package com.thanda.ecommerce.service;

import com.thanda.ecommerce.entity.CartItem;
import com.thanda.ecommerce.entity.Order;
import com.thanda.ecommerce.entity.OrderItem;
import com.thanda.ecommerce.repository.CartItemRepository;
import com.thanda.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Order placeOrder(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty())
            throw new RuntimeException("Cart is empty");

        double total = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();

        Order order = new Order(userId, total, "PENDING", LocalDateTime.now());

        List<OrderItem> items = cartItems.stream()
                .map(item -> new OrderItem(order, item.getProduct(), item.getQuantity(), item.getProduct().getPrice()))
                .collect(Collectors.toList());

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);
        cartRepository.deleteByUserId(userId);
        return savedOrder;
    }

    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus("COMPLETED");
        orderRepository.save(order);
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
