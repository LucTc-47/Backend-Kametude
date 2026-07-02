package net.codejava.business_service.dto;

import lombok.*;
import net.codejava.business_service.enums.OrderStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long clientId;
    private Long studentId;
    private Long gigId;
    private String title;
    private String description;
    private Double budget;
    private LocalDate desiredDeadline;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}