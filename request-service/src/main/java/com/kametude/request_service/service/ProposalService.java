package com.kametude.request_service.service;

import com.kametude.request_service.dto.request.CreateProposalDTO;
import com.kametude.request_service.dto.response.ProposalResponseDTO;
import com.kametude.request_service.entity.JobRequest;
import com.kametude.request_service.entity.Proposal;
import com.kametude.request_service.enums.JobRequestStatus;
import com.kametude.request_service.enums.ProposalStatus;
import com.kametude.request_service.exception.BusinessException;
import com.kametude.request_service.exception.ResourceNotFoundException;
import com.kametude.request_service.repository.JobRequestRepository;
import com.kametude.request_service.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final JobRequestRepository jobRequestRepository;

    // Soumettre une proposition
    public ProposalResponseDTO submit(Long jobRequestId, CreateProposalDTO dto) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Appel d'offre introuvable"));

        if (jobRequest.getStatus() != JobRequestStatus.OPEN) {
            throw new BusinessException("Cet appel d'offre n'accepte plus de propositions");
        }

        proposalRepository.findByJobRequestIdAndStudentId(jobRequestId, dto.getStudentId())
                .ifPresent(p -> { throw new BusinessException("Vous avez déjà soumis une proposition"); });

        Proposal proposal = Proposal.builder()
                .studentId(dto.getStudentId())
                .coverLetter(dto.getCoverLetter())
                .proposedPrice(dto.getProposedPrice())
                .deliveryDays(dto.getDeliveryDays())
                .jobRequest(jobRequest)
                .build();

        return toDTO(proposalRepository.save(proposal));
    }

    // Accepter une proposition
    @Transactional
    public ProposalResponseDTO accept(Long proposalId) {
        Proposal proposal = findById(proposalId);
        JobRequest jobRequest = proposal.getJobRequest();

        if (jobRequest.getStatus() != JobRequestStatus.OPEN) {
            throw new BusinessException("L'appel d'offre n'est plus ouvert");
        }

        // Rejeter toutes les autres propositions
        proposalRepository.findByJobRequestId(jobRequest.getId())
                .forEach(p -> {
                    if (!p.getId().equals(proposalId)) {
                        p.setStatus(ProposalStatus.REJECTED);
                        proposalRepository.save(p);
                    }
                });

        // Accepter celle-ci
        proposal.setStatus(ProposalStatus.ACCEPTED);
        jobRequest.setStatus(JobRequestStatus.IN_PROGRESS);
        jobRequestRepository.save(jobRequest);

        return toDTO(proposalRepository.save(proposal));
    }

    // Retirer une proposition
    public ProposalResponseDTO withdraw(Long proposalId) {
        Proposal proposal = findById(proposalId);

        if (proposal.getStatus() != ProposalStatus.PENDING) {
            throw new BusinessException("Seule une proposition PENDING peut être retirée");
        }

        proposal.setStatus(ProposalStatus.WITHDRAWN);
        return toDTO(proposalRepository.save(proposal));
    }

    // Lister les propositions d'un appel d'offre
    public List<ProposalResponseDTO> getByJobRequest(Long jobRequestId) {
        return proposalRepository.findByJobRequestId(jobRequestId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Lister les propositions d'un étudiant
    public List<ProposalResponseDTO> getByStudent(Long studentId) {
        return proposalRepository.findByStudentId(studentId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private Proposal findById(Long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposition introuvable avec l'ID : " + id));
    }

    private ProposalResponseDTO toDTO(Proposal p) {
        return ProposalResponseDTO.builder()
                .id(p.getId())
                .studentId(p.getStudentId())
                .jobRequestId(p.getJobRequest().getId())
                .coverLetter(p.getCoverLetter())
                .proposedPrice(p.getProposedPrice())
                .deliveryDays(p.getDeliveryDays())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }
}