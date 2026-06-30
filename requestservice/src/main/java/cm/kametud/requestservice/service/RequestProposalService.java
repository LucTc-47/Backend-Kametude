package cm.kametud.requestservice.service;

import cm.kametud.requestservice.dto.CreateProposalDTO;
import cm.kametud.requestservice.dto.ProposalDTO;
import cm.kametud.requestservice.enums.ProposalStatus;
import cm.kametud.requestservice.enums.RequestStatus;
import cm.kametud.requestservice.model.GigRequest;
import cm.kametud.requestservice.model.RequestProposal;
import cm.kametud.requestservice.repository.GigRequestRepository;
import cm.kametud.requestservice.repository.RequestProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestProposalService {

    private final RequestProposalRepository repository;
    private final GigRequestRepository gigRequestRepository;

    public RequestProposal create(CreateProposalDTO dto) {
        GigRequest request = gigRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (request.getStatus() == RequestStatus.FERME) {
            throw new RuntimeException("Cette demande est fermée");
        }

        RequestProposal proposal = new RequestProposal();
        proposal.setRequestId(dto.getRequestId());
        proposal.setStudentId(dto.getStudentId());
        proposal.setStudentName(dto.getStudentName());
        proposal.setMessage(dto.getMessage());
        proposal.setPrice(dto.getPrice());
        proposal.setDeliveryDays(dto.getDeliveryDays());
        proposal.setStatus(ProposalStatus.EN_ATTENTE);
        return repository.save(proposal);
    }

    public List<ProposalDTO> getByRequestId(UUID requestId) {
        return repository.findByRequestId(requestId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProposalDTO> getByStudentId(UUID studentId) {
        return repository.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProposalDTO acceptProposal(UUID proposalId) {
        RequestProposal proposal = repository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));

        if (proposal.getStatus() != ProposalStatus.EN_ATTENTE) {
            throw new RuntimeException("Cette proposition n'est plus en attente");
        }

        // Rejeter les autres propositions
        List<RequestProposal> others = repository.findByRequestIdAndStatus(
                proposal.getRequestId(), ProposalStatus.EN_ATTENTE);
        others.forEach(p -> p.setStatus(ProposalStatus.REFUSEE));
        repository.saveAll(others);

        // Accepter celle-ci
        proposal.setStatus(ProposalStatus.ACCEPTEE);
        RequestProposal saved = repository.save(proposal);

        // Fermer la demande
        GigRequest request = gigRequestRepository.findById(proposal.getRequestId())
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        request.setStatus(RequestStatus.FERME);
        gigRequestRepository.save(request);

        return toDTO(saved);
    }

    public void rejectProposal(UUID proposalId) {
        RequestProposal proposal = repository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));
        proposal.setStatus(ProposalStatus.REFUSEE);
        repository.save(proposal);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private ProposalDTO toDTO(RequestProposal proposal) {
        ProposalDTO dto = new ProposalDTO();
        dto.setId(proposal.getId());
        dto.setRequestId(proposal.getRequestId());
        dto.setStudentId(proposal.getStudentId());
        dto.setStudentName(proposal.getStudentName());
        dto.setMessage(proposal.getMessage());
        dto.setPrice(proposal.getPrice());
        dto.setDeliveryDays(proposal.getDeliveryDays());
        dto.setStatus(proposal.getStatus().name());
        dto.setCreatedAt(proposal.getCreatedAt());
        dto.setUpdatedAt(proposal.getUpdatedAt());
        return dto;
    }
}