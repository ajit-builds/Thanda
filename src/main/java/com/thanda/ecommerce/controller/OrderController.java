package com.thanda.ecommerce.controller;

import com.thanda.ecommerce.entity.Order;
import com.thanda.ecommerce.entity.User;
import com.thanda.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.placeOrder(user.getId()));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> pay(@PathVariable Long orderId) {
        service.processPayment(orderId);
        return ResponseEntity.ok("Payment processed successfully");
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getOrders(user.getId()));
    }
}
