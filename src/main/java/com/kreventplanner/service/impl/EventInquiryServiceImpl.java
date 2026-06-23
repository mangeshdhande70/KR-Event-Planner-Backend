package com.kreventplanner.service.impl;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import com.kreventplanner.repository.EventInquiryRepository;
import com.kreventplanner.service.EventInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventInquiryServiceImpl implements EventInquiryService {

    private final EventInquiryRepository eventInquiryRepository;

    @Autowired
    public EventInquiryServiceImpl(EventInquiryRepository eventInquiryRepository) {
        this.eventInquiryRepository = eventInquiryRepository;
    }

    @Override
    public String saveInquiry(EventInquiry inquiry) {
        if (inquiry.getCreatedAt() == null) {
            inquiry.setCreatedAt(LocalDateTime.now());
        }
        inquiry.setStatus(InquiryStatus.NEW);
        eventInquiryRepository.save(inquiry);
        return "Inquiry saved successfully";
    }

    @Override
    public List<EventInquiry> getAllInquiries() {
        return eventInquiryRepository.findAll();
    }

    @Override
    public EventInquiry updateInquiryStatus(Long id, InquiryStatus status) {
        EventInquiry inquiry = eventInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found with id " + id));
        inquiry.setStatus(status);
        return eventInquiryRepository.save(inquiry);
    }
}
