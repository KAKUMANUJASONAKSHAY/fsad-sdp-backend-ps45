package com.klef.fsad.sdp.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.entity.OtpVerification;
import com.klef.fsad.sdp.repository.OtpVerificationRepository;

import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/otp")
public class OtpController
{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final SecureRandom secureRandom = new SecureRandom();

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request)
    {
        try
        {
            String otp = String.valueOf(100000 + secureRandom.nextInt(900000));

            OtpVerification otpVerification = new OtpVerification();
            otpVerification.setEmail(request.getEmail());
            otpVerification.setOtp(otp);
            otpVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            otpRepository.save(otpVerification);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail);
            helper.setTo(request.getEmail());
            helper.setSubject("OTP Verification");

            String html =
                    "<h2>OTP Verification</h2>" +
                    "<p>Your OTP is:</p>" +
                    "<h1 style='letter-spacing:4px;color:#ff5722;'>" + otp + "</h1>" +
                    "<p>This OTP is valid for 5 minutes.</p>";

            helper.setText(html, true);
            mailSender.send(message);

            return ResponseEntity.ok("OTP Sent Successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Sending OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request)
    {
        OtpVerification data = otpRepository.findById(request.getEmail()).orElse(null);

        if (data == null)
        {
            return ResponseEntity.status(400).body("OTP Not Found or Expired");
        }

        if (LocalDateTime.now().isAfter(data.getExpiryTime()))
        {
            otpRepository.deleteById(request.getEmail());
            return ResponseEntity.status(400).body("OTP Expired");
        }

        if (!data.getOtp().equals(request.getOtp()))
        {
            return ResponseEntity.status(400).body("Invalid OTP");
        }

        otpRepository.deleteById(request.getEmail());
        return ResponseEntity.ok("OTP Verified Successfully");
    }

    static class OtpRequest
    {
        private String email;

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }
    }

    static class OtpVerifyRequest
    {
        private String email;
        private String otp;

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getOtp()
        {
            return otp;
        }

        public void setOtp(String otp)
        {
            this.otp = otp;
        }
    }

}
