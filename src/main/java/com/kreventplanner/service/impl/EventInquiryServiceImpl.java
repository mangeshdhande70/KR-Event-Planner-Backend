package com.kreventplanner.service.impl;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import com.kreventplanner.repository.EventInquiryRepository;
import com.kreventplanner.service.EventInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kreventplanner.util.EmailSenderUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class EventInquiryServiceImpl implements EventInquiryService {

    private final EventInquiryRepository eventInquiryRepository;
    private final EmailSenderUtil emailSenderUtil;

    @Autowired
    public EventInquiryServiceImpl(EventInquiryRepository eventInquiryRepository, EmailSenderUtil emailSenderUtil) {
        this.eventInquiryRepository = eventInquiryRepository;
        this.emailSenderUtil = emailSenderUtil;
    }

    @Override
    public String saveInquiry(EventInquiry inquiry) {
        if (inquiry.getCreatedAt() == null) {
            inquiry.setCreatedAt(LocalDateTime.now());
        }
        inquiry.setStatus(InquiryStatus.NEW);
        eventInquiryRepository.save(inquiry);
        
        // Send email asynchronously
        emailSenderUtil.sendInquiryAcknowledgment(inquiry);
        
        return "Inquiry saved successfully";
    }

    @Override
    public List<EventInquiry> getAllInquiries() {
        return eventInquiryRepository.findAll();
    }

    @Override
    public List<EventInquiry> getFilteredInquiries(String eventType, InquiryStatus status) {
        List<EventInquiry> results;

        boolean hasEventType = eventType != null && !eventType.isBlank() && !eventType.equalsIgnoreCase("All");
        boolean hasStatus = status != null;

        if (hasEventType && hasStatus) {
            results = eventInquiryRepository.findByEventTypeAndStatus(eventType, status);
        } else if (hasEventType) {
            results = eventInquiryRepository.findByEventType(eventType);
        } else if (hasStatus) {
            results = eventInquiryRepository.findByStatus(status);
        } else {
            results = eventInquiryRepository.findAll();
        }

        // Sort newest first
        results.sort(Comparator.comparing(EventInquiry::getCreatedAt,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return results;
    }

    @Override
    public EventInquiry updateInquiryStatus(Long id, InquiryStatus status) {
        EventInquiry inquiry = eventInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found with id " + id));
        inquiry.setStatus(status);
        return eventInquiryRepository.save(inquiry);
    }
}

