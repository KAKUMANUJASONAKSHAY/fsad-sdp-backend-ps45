package com.klef.fsad.sdp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.klef.fsad.sdp.entity.Admin;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.repository.AdminRepository;
import com.klef.fsad.sdp.repository.StudentRepository;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException
    {
        // 1. Try Admin
        Optional<Admin> adminOpt = adminRepo.findById(input);
        if (adminOpt.isPresent())
        {
            Admin admin = adminOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    admin.getUsername(),
                    admin.getPassword(),
                    List.of(new SimpleGrantedAuthority("ADMIN"))
            );
        }

        // 2. Try Student - using email
        Optional<Student> studentOpt = studentRepo.findByEmail(input);
        if (studentOpt.isPresent())
        {
            Student student = studentOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    student.getEmail(),
                    student.getPassword(),
                    List.of(new SimpleGrantedAuthority("STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with input: " + input);
    }

    @Override
    public Object getUserByLogin(String input)
    {
        Optional<Admin> adminOpt = adminRepo.findById(input);
        if (adminOpt.isPresent()) return adminOpt.get();

        Optional<Student> studentOpt = studentRepo.findByEmail(input);
        if (studentOpt.isPresent()) return studentOpt.get();

        return null;
    }
}