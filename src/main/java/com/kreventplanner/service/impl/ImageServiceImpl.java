package com.kreventplanner.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kreventplanner.dto.ImageUploadResponse;
import com.kreventplanner.dto.ImageDto;
import com.kreventplanner.dto.ImagesLinkResponse;
import com.kreventplanner.entity.EventImage;
import com.kreventplanner.repository.EventImageRepository;
import com.kreventplanner.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;
    private final EventImageRepository eventImageRepository;

    private static final List<String> ALLOWED_EVENT_TYPES = Arrays.asList("Birthday", "Wedding", "Corporate",
            "Engagement", "Baby Shower", "Ring Ceremony", "Theme Party", "Reception", "Naming Ceremony",
            "Product Launching", "House Warming", "Festival Event", "Welcome Home", "Anniversary", 
            "Retirement Party", "Annaprshan");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg",
            "image/webp");

    @Override
    @org.springframework.transaction.annotation.Transactional
    public ImageUploadResponse uploadImage(MultipartFile image, String eventType, boolean isDefault) throws IOException {
        if (!ALLOWED_EVENT_TYPES.contains(eventType)) {
            throw new IllegalArgumentException(
                    "Invalid eventType. Allowed values: Birthday, Wedding, Corporate, Engagement, Baby Shower, Ring Ceremony, Theme Party, Reception, Naming Ceremony, Product Launching, House Warming, Festival Event, Welcome Home, Anniversary, Retirement Party, Annaprshan");
        }

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(image.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Only JPG, JPEG, PNG, WEBP are allowed");
        }

        String folderPath = "KR-Event-Planner/" + eventType;

        System.out.println(folderPath);

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                "folder", folderPath));

        String publicId = uploadResult.get("public_id").toString();
        String imageUrl = uploadResult.get("secure_url").toString();

        EventImage eventImage = EventImage.builder()
                .eventType(eventType)
                .imageUrl(imageUrl)
                .publicId(publicId)
                .isDefault(isDefault)
                .build();
        eventImageRepository.save(eventImage);

        if (isDefault) {
            eventImageRepository.unsetDefaultForOtherImages(eventType, eventImage.getId());
        }

        return ImageUploadResponse.builder()
                .success(true)
                .message("Image uploaded successfully")
                .build();
    }

    @Override
    public ImagesLinkResponse getImagesByEventType(String eventType) {
        if (!ALLOWED_EVENT_TYPES.contains(eventType)) {
            throw new IllegalArgumentException(
                    "Invalid eventType. Allowed values: Birthday, Wedding, Corporate, Engagement, Baby Shower, Ring Ceremony, Theme Party");
        }

        List<EventImage> eventImages = eventImageRepository.findByEventType(eventType);
        List<ImageDto> imageDtos = eventImages.stream()
                .map(img -> ImageDto.builder()
                        .id(img.getId())
                        .imageUrl(applyCloudinaryOptimizations(img.getImageUrl()))
                        .isDefault(img.isDefault())
                        .build())
                .collect(Collectors.toList());

        return ImagesLinkResponse.builder()
                .images(imageDtos)
                .build();
    }

    @Override
    public void deleteImage(Long id) throws IOException {
        EventImage eventImage = eventImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + id));

        cloudinary.uploader().destroy(eventImage.getPublicId(), ObjectUtils.emptyMap());
        eventImageRepository.delete(eventImage);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void setDefaultImage(Long id) {
        EventImage eventImage = eventImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + id));

        eventImage.setDefault(true);
        eventImageRepository.save(eventImage);

        eventImageRepository.unsetDefaultForOtherImages(eventImage.getEventType(), id);
    }

    private String applyCloudinaryOptimizations(String url) {
        if (url != null && url.contains("/upload/")) {
            return url.replace("/upload/", "/upload/f_auto,q_auto/");
        }
        return url;
    }
}
