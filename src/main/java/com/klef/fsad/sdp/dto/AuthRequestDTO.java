package com.klef.fsad.sdp.dto;

import lombok.Data;

@Data
public class AuthRequestDTO
{
    private String login;    // username or email
    private String password;
    private String role;     // ADMIN / STUDENT

    
}