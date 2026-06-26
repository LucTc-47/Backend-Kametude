package com.kametude.request_service.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateJobRequestDTO {
    private String title;
    private String description;
    private String category;
    private String city;
    private Double budget;
    private LocalDateTime deadline;
}