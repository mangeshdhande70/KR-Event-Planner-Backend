package com.kreventplanner.repository;

import com.kreventplanner.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findByEventType(String eventType);
    Optional<EventImage> findFirstByEventTypeAndIsDefaultTrue(String eventType);
    Optional<EventImage> findFirstByEventTypeOrderByIdAsc(String eventType);

    @Modifying
    @Query("UPDATE EventImage e SET e.isDefault = false WHERE e.eventType = :eventType AND e.id != :id")
    void unsetDefaultForOtherImages(@Param("eventType") String eventType, @Param("id") Long id);
}
