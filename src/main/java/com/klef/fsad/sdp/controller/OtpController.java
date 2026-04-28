package com.klef.fsad.sdp.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/otp")
@CrossOrigin("*")
public class OtpController
{
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request)
    {
        try
        {
            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            otpStore.put(
                request.getEmail(),
                new OtpData(otp, LocalDateTime.now().plusMinutes(5))
            );

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
        OtpData data = otpStore.get(request.getEmail());

        if (data == null)
        {
            return ResponseEntity.status(400).body("OTP Not Found or Expired");
        }

        if (LocalDateTime.now().isAfter(data.getExpiryTime()))
        {
            otpStore.remove(request.getEmail());
            return ResponseEntity.status(400).body("OTP Expired");
        }

        if (!data.getOtp().equals(request.getOtp()))
        {
            return ResponseEntity.status(400).body("Invalid OTP");
        }

        otpStore.remove(request.getEmail());
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

    static class OtpData
    {
        private String otp;
        private LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime)
        {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp()
        {
            return otp;
        }

        public LocalDateTime getExpiryTime()
        {
            return expiryTime;
        }
    }
}
