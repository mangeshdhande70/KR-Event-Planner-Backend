package com.kreventplanner.controller;

import com.kreventplanner.dto.ImageUploadResponse;
import com.kreventplanner.dto.ImagesLinkResponse;
import com.kreventplanner.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("eventType") String eventType,
            @RequestParam(value = "isDefault", required = false, defaultValue = "false") boolean isDefault) throws IOException {

        ImageUploadResponse response = imageService.uploadImage(image, eventType, isDefault);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getImages/{eventType}")
    public ResponseEntity<ImagesLinkResponse> getImagesByEventType(@PathVariable String eventType) {
        ImagesLinkResponse response = imageService.getImagesByEventType(eventType);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting image from Cloudinary");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<Void> setDefaultImage(@PathVariable Long id) {
        imageService.setDefaultImage(id);
        return ResponseEntity.ok().build();
    }
}
