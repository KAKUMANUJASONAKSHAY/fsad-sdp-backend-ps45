package com.klef.fsad.sdp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    public Object getUserByLogin(String input);
}