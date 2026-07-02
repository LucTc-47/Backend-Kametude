package net.codejava.business_service.service;

import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.RevisionRequest;
import net.codejava.business_service.dto.RevisionResponse;
import net.codejava.business_service.entity.Revision;
import net.codejava.business_service.repository.RevisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevisionService {

    private final RevisionRepository revisionRepository;

    public RevisionResponse create(RevisionRequest request) {
        Revision revision = Revision.builder()
                .orderId(request.getOrderId())
                .reason(request.getReason())
                .build();
        return toResponse(revisionRepository.save(revision));
    }

    public List<RevisionResponse> getByOrder(Long orderId) {
        return revisionRepository.findByOrderId(orderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RevisionResponse toResponse(Revision r) {
        return RevisionResponse.builder()
                .id(r.getId())
                .orderId(r.getOrderId())
                .reason(r.getReason())
                .requestedAt(r.getRequestedAt())
                .build();
    }
}