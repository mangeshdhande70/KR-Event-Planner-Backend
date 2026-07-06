package com.kreventplanner.controller;

import com.kreventplanner.entity.EventInquiry;
import com.kreventplanner.entity.InquiryStatus;
import com.kreventplanner.service.EventInquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inquiries")
public class EventInquiryController {

    private final EventInquiryService eventInquiryService;

    @Autowired
    public EventInquiryController(EventInquiryService eventInquiryService) {
        this.eventInquiryService = eventInquiryService;
    }

    @PostMapping
    public ResponseEntity<String> createInquiry(@Valid @RequestBody EventInquiry inquiry) {
        String response = eventInquiryService.saveInquiry(inquiry);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventInquiry>> getAllInquiries() {
        return ResponseEntity.ok(eventInquiryService.getAllInquiries());
    }

    /**
     * GET /api/inquiries/filter?eventType=Wedding&status=NEW
     * Both query params are optional. When omitted, all inquiries are returned.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<EventInquiry>> getFilteredInquiries(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) InquiryStatus status) {
        return ResponseEntity.ok(eventInquiryService.getFilteredInquiries(eventType, status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EventInquiry> updateInquiryStatus(@PathVariable Long id, @RequestParam InquiryStatus status) {
        EventInquiry updatedInquiry = eventInquiryService.updateInquiryStatus(id, status);
        return ResponseEntity.ok(updatedInquiry);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
