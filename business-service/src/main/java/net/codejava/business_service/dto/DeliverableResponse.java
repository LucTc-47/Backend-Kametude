package net.codejava.business_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliverableResponse {
    private Long id;
    private Long orderId;
    private String fileUrl;
    private String description;
    private LocalDateTime submittedAt;
}