package com.kreventplanner.repository;

import com.kreventplanner.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findByEventType(String eventType);
}
