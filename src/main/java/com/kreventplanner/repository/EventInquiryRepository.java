package com.kreventplanner.repository;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventInquiryRepository extends JpaRepository<EventInquiry, Long> {

    List<EventInquiry> findByEventType(String eventType);

    List<EventInquiry> findByStatus(InquiryStatus status);

    List<EventInquiry> findByEventTypeAndStatus(String eventType, InquiryStatus status);
}
