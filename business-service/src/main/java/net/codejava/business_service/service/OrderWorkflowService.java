package net.codejava.business_service.service;

import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.OrderResponse;
import net.codejava.business_service.entity.Order;
import net.codejava.business_service.enums.OrderStatus;
import net.codejava.business_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderWorkflowService {

    private final OrderRepository orderRepository;

    public OrderResponse accept(Long id) {
        return changeStatus(id, OrderStatus.ACCEPTED, OrderStatus.PENDING);
    }

    public OrderResponse reject(Long id) {
        return changeStatus(id, OrderStatus.REJECTED, OrderStatus.PENDING);
    }

    public OrderResponse start(Long id) {
        return changeStatus(id, OrderStatus.IN_PROGRESS, OrderStatus.ACCEPTED);
    }

    public OrderResponse deliver(Long id) {
        return changeStatus(id, OrderStatus.DELIVERED, OrderStatus.IN_PROGRESS);
    }

    public OrderResponse complete(Long id) {
        return changeStatus(id, OrderStatus.COMPLETED, OrderStatus.DELIVERED);
    }

    public OrderResponse requestRevision(Long id) {
        return changeStatus(id, OrderStatus.REVISION_REQUESTED, OrderStatus.DELIVERED);
    }

    public OrderResponse dispute(Long id) {
        return changeStatus(id, OrderStatus.DISPUTED, OrderStatus.DELIVERED);
    }

    private OrderResponse changeStatus(Long id, OrderStatus newStatus, OrderStatus requiredStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id : " + id));

        if (order.getStatus() != requiredStatus) {
            throw new RuntimeException(
                "Transition invalide : la commande doit être en statut "
                + requiredStatus + " pour passer à " + newStatus
                + ". Statut actuel : " + order.getStatus()
            );
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

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