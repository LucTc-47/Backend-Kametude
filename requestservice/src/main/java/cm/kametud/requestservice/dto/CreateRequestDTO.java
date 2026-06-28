package cm.kametud.requestservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotNull(message = "Le budget est obligatoire")
    @Positive(message = "Le budget doit être positif")
    private Double budget;

    @NotBlank(message = "La catégorie est obligatoire")
    private String category;

    private String location;

    @NotNull(message = "La date limite est obligatoire")
    private LocalDateTime deadline;

    @NotNull(message = "L'ID du client est obligatoire")
    private UUID clientId;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String clientName;
}