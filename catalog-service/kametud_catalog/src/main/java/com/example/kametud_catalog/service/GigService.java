package com.example.kametud_catalog.service;

import com.example.kametud_catalog.client.IdentityClient;
import com.example.kametud_catalog.client.StudentStatusResponse;
import com.example.kametud_catalog.dto.GigCreateRequest;
import com.example.kametud_catalog.dto.GigResponse;
import com.example.kametud_catalog.entity.Gig;
import com.example.kametud_catalog.exception.GigNotFoundException;
import com.example.kametud_catalog.exception.StudentPublicationForbiddenException;
import com.example.kametud_catalog.repository.GigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final IdentityClient identityClient;

    @Transactional
    public GigResponse createGig(GigCreateRequest request) {
        if (request.isPublished()) {
            ensureStudentCanPublish(request.getStudentId());
        }

        Gig gig = Gig.builder()
                .studentId(request.getStudentId())
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .category(request.getCategory().trim())
                .location(request.getLocation().trim())
                .rating(BigDecimal.ZERO)
                .tierBasique(request.getTierBasique())
                .tierStandard(request.getTierStandard())
                .tierPremium(request.getTierPremium())
                .published(request.isPublished())
                .build();

        return GigResponse.fromEntity(gigRepository.save(gig));
    }

    @Transactional(readOnly = true)
    public List<GigResponse> searchGigs(String category, String location) {
        return gigRepository.searchPublished(normalize(category), normalize(location))
                .stream()
                .map(GigResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public GigResponse getGig(UUID gigId) {
        return GigResponse.fromEntity(getGigOrThrow(gigId));
    }

    @Transactional
    public GigResponse publishGig(UUID gigId) {
        Gig gig = getGigOrThrow(gigId);
        ensureStudentCanPublish(gig.getStudentId());

        gig.setPublished(true);

        return GigResponse.fromEntity(gigRepository.save(gig));
    }

    private Gig getGigOrThrow(UUID gigId) {
        return gigRepository.findById(gigId)
                .orElseThrow(() -> new GigNotFoundException(gigId));
    }

    private void ensureStudentCanPublish(UUID studentId) {
        StudentStatusResponse status = identityClient.getStudentStatus(studentId);
        if (!status.canPublish()) {
            throw new StudentPublicationForbiddenException(studentId);
        }
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
