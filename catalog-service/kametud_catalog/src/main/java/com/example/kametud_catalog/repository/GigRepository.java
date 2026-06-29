package com.example.kametud_catalog.repository;

import com.example.kametud_catalog.entity.Gig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GigRepository extends JpaRepository<Gig, UUID> {

    @Query("""
            select g
            from Gig g
            where g.published = true
              and (:category is null or lower(g.category) = lower(:category))
              and (:location is null or lower(g.location) like lower(concat('%', :location, '%')))
            order by g.createdAt desc
            """)
    List<Gig> searchPublished(
            @Param("category") String category,
            @Param("location") String location
    );
}
