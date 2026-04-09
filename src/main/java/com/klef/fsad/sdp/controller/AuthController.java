package com.klef.fsad.sdp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.dto.AuthRequestDTO;
import com.klef.fsad.sdp.security.JwtUtil;
import com.klef.fsad.sdp.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController
{
    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request)
    {
        try
        {
            UserDetails userDetails = service.loadUserByUsername(request.getLogin());

            String role = userDetails.getAuthorities()
                    .iterator().next().getAuthority();

            if (!role.equalsIgnoreCase(request.getRole()))
            {
                return ResponseEntity.status(403).body("Invalid Role");
            }

            boolean isValid = false;

            if (role.equalsIgnoreCase("ADMIN"))
            {
                isValid = request.getPassword().equals(userDetails.getPassword());
            }
            else if (role.equalsIgnoreCase("STUDENT"))
            {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                isValid = encoder.matches(request.getPassword(), userDetails.getPassword());
            }
            else
            {
                return ResponseEntity.status(403).body("Invalid Role Type");
            }

            if (!isValid)
            {
                return ResponseEntity.status(401).body("Login Invalid");
            }

            String token = jwtUtil.generateToken(userDetails);
            Object userObj = service.getUserByLogin(request.getLogin());

            return ResponseEntity.ok(
                Map.of(
                    "token", token,
                    "role", role,
                    "user", userObj
                )
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}