package net.codejava.business_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevisionResponse {
    private Long id;
    private Long orderId;
    private String reason;
    private LocalDateTime requestedAt;
}