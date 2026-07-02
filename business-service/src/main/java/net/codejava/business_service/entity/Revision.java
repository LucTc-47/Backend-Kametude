package net.codejava.business_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "revisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDateTime requestedAt;

    @PrePersist
    public void prePersist() {
        requestedAt = LocalDateTime.now();
    }
}