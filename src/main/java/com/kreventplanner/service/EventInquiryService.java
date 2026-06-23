package com.kreventplanner.service;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import java.util.List;

public interface EventInquiryService {
    String saveInquiry(EventInquiry inquiry);
    List<EventInquiry> getAllInquiries();
    EventInquiry updateInquiryStatus(Long id, InquiryStatus status);
}
