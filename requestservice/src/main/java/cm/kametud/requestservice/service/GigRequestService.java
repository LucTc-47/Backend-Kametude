package cm.kametud.requestservice.service;

import cm.kametud.requestservice.dto.CreateRequestDTO;
import cm.kametud.requestservice.dto.GigRequestDTO;
import cm.kametud.requestservice.enums.RequestStatus;
import cm.kametud.requestservice.model.GigRequest;
import cm.kametud.requestservice.repository.GigRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GigRequestService {

    private final GigRequestRepository repository;

    public GigRequest create(CreateRequestDTO dto) {
        GigRequest request = new GigRequest();
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setBudget(dto.getBudget());
        request.setCategory(dto.getCategory());
        request.setLocation(dto.getLocation());
        request.setDeadline(dto.getDeadline());
        request.setClientId(dto.getClientId());
        request.setClientName(dto.getClientName());
        request.setStatus(RequestStatus.OUVERT);
        return repository.save(request);
    }

    public List<GigRequestDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public GigRequestDTO getById(UUID id) {
        GigRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        return toDTO(request);
    }

    public List<GigRequest> getByStatus(RequestStatus status) {
        return repository.findByStatus(status);
    }

    public GigRequest closeRequest(UUID id) {
        GigRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        request.setStatus(RequestStatus.FERME);
        return repository.save(request);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private GigRequestDTO toDTO(GigRequest request) {
        GigRequestDTO dto = new GigRequestDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setBudget(request.getBudget());
        dto.setCategory(request.getCategory());
        dto.setLocation(request.getLocation());
        dto.setDeadline(request.getDeadline());
        dto.setStatus(request.getStatus().name());
        dto.setClientId(request.getClientId());
        dto.setClientName(request.getClientName());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}