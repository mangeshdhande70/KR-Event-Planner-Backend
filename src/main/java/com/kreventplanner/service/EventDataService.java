package com.kreventplanner.service;

import com.kreventplanner.dto.EventDataDto;
import java.util.List;

public interface EventDataService {
    List<EventDataDto> getAllEvents();
    EventDataDto getEventById(String eventId);
}
