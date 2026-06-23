package com.kreventplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUploadResponse {
    private boolean success;
    private String message;
    private String eventType;
    private String imageUrl;
    private String publicId;
}
