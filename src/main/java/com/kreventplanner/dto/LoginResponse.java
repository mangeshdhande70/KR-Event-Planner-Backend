package com.kreventplanner.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private boolean success;
    private String message;
    private String username;
    private String role;
    private String token;
}
