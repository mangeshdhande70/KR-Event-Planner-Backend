package com.kreventplanner.service.impl;

import com.kreventplanner.dto.EventDataDto;
import com.kreventplanner.entity.EventImage;
import com.kreventplanner.repository.EventImageRepository;
import com.kreventplanner.service.EventDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventDataServiceImpl implements EventDataService {

    private final EventImageRepository eventImageRepository;

    private static final Map<String, String> EVENT_TYPE_MAP = new LinkedHashMap<>();
    static {
        EVENT_TYPE_MAP.put("corporate-events", "Corporate");
        EVENT_TYPE_MAP.put("luxury-weddings", "Wedding");
        EVENT_TYPE_MAP.put("theme-parties", "Theme Party");
        EVENT_TYPE_MAP.put("birthday-parties", "Birthday");
        EVENT_TYPE_MAP.put("ring-ceremony", "Ring Ceremony");
        EVENT_TYPE_MAP.put("baby-shower", "Baby Shower");
        EVENT_TYPE_MAP.put("reception", "Reception");
        EVENT_TYPE_MAP.put("naming-ceremony", "Naming Ceremony");
        EVENT_TYPE_MAP.put("product-launching", "Product Launching");
        EVENT_TYPE_MAP.put("house-warming", "House Warming");
        EVENT_TYPE_MAP.put("festival-event", "Festival Event");
        EVENT_TYPE_MAP.put("welcome-home", "Welcome Home");
        EVENT_TYPE_MAP.put("anniversary", "Anniversary");
        EVENT_TYPE_MAP.put("retirement-party", "Retirement Party");
        EVENT_TYPE_MAP.put("annaprshan", "Annaprshan");
    }

    @Override
    public List<EventDataDto> getAllEvents() {
        List<EventDataDto> dtos = new ArrayList<>();
        for (String eventId : EVENT_TYPE_MAP.keySet()) {
            dtos.add(getEventById(eventId));
        }
        return dtos;
    }

    @Override
    public EventDataDto getEventById(String eventId) {
        String backendEventType = EVENT_TYPE_MAP.get(eventId);
        if (backendEventType == null) {
            backendEventType = "Wedding"; // fallback
        }

        // Try to get default image
        Optional<EventImage> heroOpt = eventImageRepository.findFirstByEventTypeAndIsDefaultTrue(backendEventType);
        
        // Fallback to first image if no default is designated
        if (heroOpt.isEmpty()) {
            heroOpt = eventImageRepository.findFirstByEventTypeOrderByIdAsc(backendEventType);
        }

        String heroUrl = "";
        if (heroOpt.isPresent()) {
            String originalUrl = heroOpt.get().getImageUrl();
            // Apply Cloudinary CDN optimizations
            heroUrl = applyCloudinaryOptimizations(originalUrl);
        }

        return EventDataDto.builder()
                .id(eventId)
                .heroImage(heroUrl)
                .build();
    }

    private String applyCloudinaryOptimizations(String url) {
        if (url != null && url.contains("/upload/")) {
            return url.replace("/upload/", "/upload/f_auto,q_auto/");
        }
        return url;
    }
}
