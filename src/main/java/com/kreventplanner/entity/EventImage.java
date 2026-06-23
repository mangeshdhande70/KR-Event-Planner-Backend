package com.kreventplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private String publicId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;
}
