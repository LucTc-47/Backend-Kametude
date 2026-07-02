package net.codejava.business_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliverableRequest {

    @NotNull(message = "orderId est obligatoire")
    private Long orderId;

    private String fileUrl;

    private String description;
}