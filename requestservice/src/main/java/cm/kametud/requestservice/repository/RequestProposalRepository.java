package cm.kametud.requestservice.repository;

import cm.kametud.requestservice.model.RequestProposal;
import cm.kametud.requestservice.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestProposalRepository extends JpaRepository<RequestProposal, UUID> {
    List<RequestProposal> findByRequestId(UUID requestId);
    List<RequestProposal> findByStudentId(UUID studentId);
    List<RequestProposal> findByRequestIdAndStatus(UUID requestId, ProposalStatus status);
}