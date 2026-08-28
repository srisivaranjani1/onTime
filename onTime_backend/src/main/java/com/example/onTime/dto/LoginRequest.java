package com.example.onTime.dto;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}