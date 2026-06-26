package com.kametude.request_service.repository;

import com.kametude.request_service.entity.Proposal;
import com.kametude.request_service.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    // Toutes les propositions d'un appel d'offre
    List<Proposal> findByJobRequestId(Long jobRequestId);

    // Toutes les propositions d'un étudiant
    List<Proposal> findByStudentId(Long studentId);

    // Vérifier si un étudiant a déjà proposé sur un appel d'offre
    Optional<Proposal> findByJobRequestIdAndStudentId(Long jobRequestId, Long studentId);

    // Toutes les propositions d'un appel d'offre par statut
    List<Proposal> findByJobRequestIdAndStatus(Long jobRequestId, ProposalStatus status);
}