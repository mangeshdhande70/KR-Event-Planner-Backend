package com.kreventplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDataDto {
    private String id;
    private String title;
    private String tagline;
    private String description;
    private List<String> tags;
    private String iconClass;
    private String heroImage;
}
