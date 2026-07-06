package com.kreventplanner.service;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryMcpTool {

    private final EventInquiryService eventInquiryService;

    public EnquiryMcpTool(EventInquiryService eventInquiryService) {
        this.eventInquiryService = eventInquiryService;
    }

    @Tool(description = "Get all new event enquiries that have not been processed yet.")
    public List<EventInquiry> getNewEnquiries() {
        return eventInquiryService.getFilteredInquiries(null, InquiryStatus.NEW);
    }
}
