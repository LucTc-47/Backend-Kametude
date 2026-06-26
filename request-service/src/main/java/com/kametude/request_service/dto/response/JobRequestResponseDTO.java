package com.kametude.request_service.dto.response;

import com.kametude.request_service.enums.JobRequestStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobRequestResponseDTO {
    private Long id;
    private Long clientId;
    private String title;
    private String description;
    private String category;
    private String city;
    private Double budget;
    private LocalDateTime deadline;
    private JobRequestStatus status;
    private LocalDateTime createdAt;
    private List<ProposalResponseDTO> proposals;
}