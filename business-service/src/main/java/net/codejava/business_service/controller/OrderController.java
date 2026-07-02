package net.codejava.business_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.OrderRequest;
import net.codejava.business_service.dto.OrderResponse;
import net.codejava.business_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST /api/orders — Créer une commande
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/orders — Lister toutes les commandes
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/{id} — Voir le détail d'une commande
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // GET /api/orders/client/{clientId} — Commandes d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClient(clientId));
    }

    // GET /api/orders/student/{studentId} — Commandes d'un étudiant
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(orderService.getOrdersByStudent(studentId));
    }
}