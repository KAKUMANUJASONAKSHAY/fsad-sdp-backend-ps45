package com.klef.fsad.sdp.dto;

public class AuthRequestDTO
{
    private String login;    // username or email
    private String password;
    private String role;     // ADMIN / STUDENT

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}