package com.klef.fsad.sdp.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.klef.fsad.sdp.entity.Admin;
import com.klef.fsad.sdp.repository.AdminRepository;

@Configuration
public class DefaultAdminInitializer
{
    @Bean
    CommandLineRunner seedDefaultAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder)
    {
        return args -> {
            if (adminRepository.existsById("admin"))
            {
                return;
            }

            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            adminRepository.save(admin);
        };
    }
}
