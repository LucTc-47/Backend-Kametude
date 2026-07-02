package net.codejava.business_service.repository;

import net.codejava.business_service.entity.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RevisionRepository extends JpaRepository<Revision, Long> {
    List<Revision> findByOrderId(Long orderId);
}