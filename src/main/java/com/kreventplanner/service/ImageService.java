package com.kreventplanner.service;

import com.kreventplanner.dto.ImageUploadResponse;
import com.kreventplanner.dto.ImagesLinkResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageUploadResponse uploadImage(MultipartFile image, String eventType) throws IOException;
    ImagesLinkResponse getImagesByEventType(String eventType);
    void deleteImage(Long id) throws IOException;
}
