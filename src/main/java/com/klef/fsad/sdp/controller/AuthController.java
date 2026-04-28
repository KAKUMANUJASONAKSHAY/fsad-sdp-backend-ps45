package com.klef.fsad.sdp.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.dto.AuthRequestDTO;
import com.klef.fsad.sdp.entity.Admin;
import com.klef.fsad.sdp.entity.Faculty;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.security.JwtUtil;
import com.klef.fsad.sdp.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request)
    {
        try
        {
            if (request.getLogin() == null || request.getPassword() == null || request.getRole() == null)
            {
                return ResponseEntity.badRequest().body("Login, password and role are required");
            }

            UserDetails userDetails = service.loadUserByUsername(request.getLogin());

            String role = userDetails.getAuthorities()
                    .iterator().next().getAuthority();

            if (!role.equalsIgnoreCase(request.getRole()))
            {
                return ResponseEntity.status(403).body("Invalid Role");
            }

            if (!role.equalsIgnoreCase("ADMIN")
                    && !role.equalsIgnoreCase("STUDENT")
                    && !role.equalsIgnoreCase("FACULTY"))
            {
                return ResponseEntity.status(403).body("Invalid Role Type");
            }

            boolean isValid = passwordEncoder.matches(request.getPassword(), userDetails.getPassword());

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
                    "user", toSafeUserResponse(userObj)
                )
            );
        }
        catch (UsernameNotFoundException e)
        {
            return ResponseEntity.status(401).body("Login Invalid");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private Map<String, Object> toSafeUserResponse(Object userObj)
    {
        Map<String, Object> user = new LinkedHashMap<>();

        if (userObj instanceof Admin admin)
        {
            user.put("username", admin.getUsername());
            return user;
        }

        if (userObj instanceof Student student)
        {
            user.put("id", student.getId());
            user.put("name", student.getName());
            user.put("email", student.getEmail());
            user.put("username", student.getUsername());
            user.put("contact", student.getContact());
            user.put("department", student.getDepartment());
            user.put("rollNumber", student.getRollNumber());
            user.put("gender", student.getGender());
            user.put("registeredAt", student.getRegisteredAt());
            return user;
        }

        if (userObj instanceof Faculty faculty)
        {
            user.put("id", faculty.getId());
            user.put("name", faculty.getName());
            user.put("email", faculty.getEmail());
            user.put("username", faculty.getUsername());
            user.put("contact", faculty.getContact());
            user.put("department", faculty.getDepartment());
            user.put("designation", faculty.getDesignation());
            return user;
        }

        return user;
    }
}
