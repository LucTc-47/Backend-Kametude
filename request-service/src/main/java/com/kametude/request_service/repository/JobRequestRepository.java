package com.kametude.request_service.repository;

import com.kametude.request_service.entity.JobRequest;
import com.kametude.request_service.enums.JobRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

    // Retrouver tous les appels d'offre d'un client
    List<JobRequest> findByClientId(Long clientId);

    // Retrouver tous les appels d'offre par statut
    List<JobRequest> findByStatus(JobRequestStatus status);

    // Retrouver tous les appels d'offre d'un client par statut
    List<JobRequest> findByClientIdAndStatus(Long clientId, JobRequestStatus status);
}