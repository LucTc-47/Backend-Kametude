package net.codejava.business_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "clientId est obligatoire")
    private Long clientId;

    @NotNull(message = "studentId est obligatoire")
    private Long studentId;

    @NotNull(message = "gigId est obligatoire")
    private Long gigId;

    @NotNull(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotNull(message = "Le budget est obligatoire")
    @Positive(message = "Le budget doit être positif")
    private Double budget;

    private LocalDate desiredDeadline;
}