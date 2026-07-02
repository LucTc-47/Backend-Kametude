package net.codejava.business_service.service;

import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.DeliverableRequest;
import net.codejava.business_service.dto.DeliverableResponse;
import net.codejava.business_service.entity.Deliverable;
import net.codejava.business_service.repository.DeliverableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliverableService {

    private final DeliverableRepository deliverableRepository;

    public DeliverableResponse submit(DeliverableRequest request) {
        Deliverable deliverable = Deliverable.builder()
                .orderId(request.getOrderId())
                .fileUrl(request.getFileUrl())
                .description(request.getDescription())
                .build();
        return toResponse(deliverableRepository.save(deliverable));
    }

    public List<DeliverableResponse> getByOrder(Long orderId) {
        return deliverableRepository.findByOrderId(orderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DeliverableResponse toResponse(Deliverable d) {
        return DeliverableResponse.builder()
                .id(d.getId())
                .orderId(d.getOrderId())
                .fileUrl(d.getFileUrl())
                .description(d.getDescription())
                .submittedAt(d.getSubmittedAt())
                .build();
    }
}