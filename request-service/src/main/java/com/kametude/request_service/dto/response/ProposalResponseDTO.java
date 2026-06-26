package com.kametude.request_service.dto.response;

import com.kametude.request_service.enums.ProposalStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProposalResponseDTO {
    private Long id;
    private Long studentId;
    private Long jobRequestId;
    private String coverLetter;
    private Double proposedPrice;
    private Integer deliveryDays;
    private ProposalStatus status;
    private LocalDateTime createdAt;
}