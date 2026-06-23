package com.kreventplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
@Table(name = "event_inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+91|91|0)?[6-9]\\d{9}$", message = "Invalid Indian phone number")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Event details are required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String eventDetails;

    @Column(nullable = false)
    private String eventType = "Wedding";

    @FutureOrPresent(message = "Event date must be today or in the future")
    @Column(name = "event_date")
    private LocalDate eventDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status = InquiryStatus.NEW;

    private LocalDateTime createdAt = LocalDateTime.now();
}
