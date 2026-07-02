package net.codejava.business_service.service;

import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.OrderRequest;
import net.codejava.business_service.dto.OrderResponse;
import net.codejava.business_service.entity.Order;
import net.codejava.business_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // Créer une commande
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .clientId(request.getClientId())
                .studentId(request.getStudentId())
                .gigId(request.getGigId())
                .title(request.getTitle())
                .description(request.getDescription())
                .budget(request.getBudget())
                .desiredDeadline(request.getDesiredDeadline())
                .build();
        // status et createdAt sont gérés automatiquement par @PrePersist
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    // Lister toutes les commandes
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Voir le détail d'une commande
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id : " + id));
        return toResponse(order);
    }

    // Lister les commandes d'un client
    public List<OrderResponse> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Lister les commandes d'un étudiant
    public List<OrderResponse> getOrdersByStudent(Long studentId) {
        return orderRepository.findByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Mapper Order → OrderResponse
    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .clientId(order.getClientId())
                .studentId(order.getStudentId())
                .gigId(order.getGigId())
                .title(order.getTitle())
                .description(order.getDescription())
                .budget(order.getBudget())
                .desiredDeadline(order.getDesiredDeadline())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}