package com.kreventplanner.controller;

import com.kreventplanner.dto.EventDataDto;
import com.kreventplanner.service.EventDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventDataController {

    private final EventDataService eventDataService;

    @GetMapping
    public ResponseEntity<List<EventDataDto>> getAllEvents() {
        return ResponseEntity.ok(eventDataService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDataDto> getEventById(@PathVariable String id) {
        return ResponseEntity.ok(eventDataService.getEventById(id));
    }
}
