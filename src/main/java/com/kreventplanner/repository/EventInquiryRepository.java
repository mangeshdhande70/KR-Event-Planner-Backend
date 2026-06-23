package com.kreventplanner.repository;

import com.kreventplanner.entity.EventInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventInquiryRepository extends JpaRepository<EventInquiry, Long> {
}
