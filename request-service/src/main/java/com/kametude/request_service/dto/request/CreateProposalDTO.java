package com.kametude.request_service.dto.request;

import lombok.Data;

@Data
public class CreateProposalDTO {
    private Long studentId;
    private String coverLetter;
    private Double proposedPrice;
    private Integer deliveryDays;
}