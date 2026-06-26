package com.kametude.request_service.service;

import com.kametude.request_service.dto.request.CreateJobRequestDTO;
import com.kametude.request_service.dto.request.UpdateJobRequestDTO;
import com.kametude.request_service.dto.response.JobRequestResponseDTO;
import com.kametude.request_service.dto.response.ProposalResponseDTO;
import com.kametude.request_service.entity.JobRequest;
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
public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final ProposalRepository proposalRepository;

    // Créer un appel d'offre
    public JobRequestResponseDTO create(Long clientId, CreateJobRequestDTO dto) {
        JobRequest jobRequest = JobRequest.builder()
                .clientId(clientId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .city(dto.getCity())
                .budget(dto.getBudget())
                .deadline(dto.getDeadline())
                .build();

        return toDTO(jobRequestRepository.save(jobRequest));
    }

    // Récupérer tous les appels d'offre ouverts
    public List<JobRequestResponseDTO> getAllOpen() {
        return jobRequestRepository.findByStatus(JobRequestStatus.OPEN)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Récupérer un appel d'offre par ID
    public JobRequestResponseDTO getById(Long id) {
        return toDTO(findById(id));
    }

    // Récupérer les appels d'offre d'un client
    public List<JobRequestResponseDTO> getByClient(Long clientId) {
        return jobRequestRepository.findByClientId(clientId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Mettre à jour un appel d'offre
    public JobRequestResponseDTO update(Long id, UpdateJobRequestDTO dto) {
        JobRequest jobRequest = findById(id);

        if (jobRequest.getStatus() != JobRequestStatus.OPEN) {
            throw new BusinessException("Seul un appel d'offre OPEN peut être modifié");
        }

        jobRequest.setTitle(dto.getTitle());
        jobRequest.setDescription(dto.getDescription());
        jobRequest.setCategory(dto.getCategory());
        jobRequest.setCity(dto.getCity());
        jobRequest.setBudget(dto.getBudget());
        jobRequest.setDeadline(dto.getDeadline());

        return toDTO(jobRequestRepository.save(jobRequest));
    }

    // Annuler un appel d'offre
    public JobRequestResponseDTO cancel(Long id) {
        JobRequest jobRequest = findById(id);

        if (jobRequest.getStatus() == JobRequestStatus.CLOSED) {
            throw new BusinessException("Un appel d'offre CLOSED ne peut pas être annulé");
        }

        jobRequest.setStatus(JobRequestStatus.CANCELLED);
        return toDTO(jobRequestRepository.save(jobRequest));
    }

    // Rouvrir un appel d'offre (Option B)
    @Transactional
    public JobRequestResponseDTO reopen(Long id) {
        JobRequest jobRequest = findById(id);

        if (jobRequest.getStatus() != JobRequestStatus.IN_PROGRESS) {
            throw new BusinessException("Seul un appel d'offre IN_PROGRESS peut être rouvert");
        }

        // Rejeter la proposition acceptée
        proposalRepository.findByJobRequestIdAndStatus(id, ProposalStatus.ACCEPTED)
                .forEach(p -> {
                    p.setStatus(ProposalStatus.REJECTED);
                    proposalRepository.save(p);
                });

        // Remettre les propositions rejetées en PENDING
        proposalRepository.findByJobRequestIdAndStatus(id, ProposalStatus.REJECTED)
                .forEach(p -> {
                    p.setStatus(ProposalStatus.PENDING);
                    proposalRepository.save(p);
                });

        jobRequest.setStatus(JobRequestStatus.OPEN);
        return toDTO(jobRequestRepository.save(jobRequest));
    }

    // Méthode utilitaire
    private JobRequest findById(Long id) {
        return jobRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appel d'offre introuvable avec l'ID : " + id));
    }

    // Convertir entité → DTO
    private JobRequestResponseDTO toDTO(JobRequest j) {
        List<ProposalResponseDTO> proposals = j.getProposals() == null ? List.of() :
                j.getProposals().stream().map(p -> ProposalResponseDTO.builder()
                        .id(p.getId())
                        .studentId(p.getStudentId())
                        .jobRequestId(j.getId())
                        .coverLetter(p.getCoverLetter())
                        .proposedPrice(p.getProposedPrice())
                        .deliveryDays(p.getDeliveryDays())
                        .status(p.getStatus())
                        .createdAt(p.getCreatedAt())
                        .build()).collect(Collectors.toList());

        return JobRequestResponseDTO.builder()
                .id(j.getId())
                .clientId(j.getClientId())
                .title(j.getTitle())
                .description(j.getDescription())
                .category(j.getCategory())
                .city(j.getCity())
                .budget(j.getBudget())
                .deadline(j.getDeadline())
                .status(j.getStatus())
                .createdAt(j.getCreatedAt())
                .proposals(proposals)
                .build();
    }
}