package com.klef.fsad.sdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.dto.EmailDTO;

import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/mail")
public class MailController
{
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO mailDTO)
    {
        try
        {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            int otp = (int) (Math.random() * 99999);

            helper.setTo(mailDTO.getReceiveremail());
            helper.setSubject(mailDTO.getSubject());
            helper.setFrom(senderEmail);

            String htmlContent =
                    "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background: #ffffff; border-radius: 10px; padding: 20px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>" +
                    "<h2 style='text-align: center; color: #4CAF50;'>Contact Form Details</h2>" +
                    "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>" +
                    "<tr><td style='padding: 10px; font-weight: bold; background: #f9f9f9;'>Name</td><td style='padding: 10px;'>" + mailDTO.getFullname() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold; background: #f9f9f9;'>Email</td><td style='padding: 10px;'>" + mailDTO.getReceiveremail() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold; background: #f9f9f9;'>Subject</td><td style='padding: 10px;'>" + mailDTO.getSubject() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold; background: #f9f9f9;'>Message</td><td style='padding: 10px;'>" + mailDTO.getMessage() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold; background: #f9f9f9;'>Contact</td><td style='padding: 10px;'>" + mailDTO.getContact() + "</td></tr>" +
                    "</table>" +
                    "<div style='margin-top: 20px; text-align: center;'>" +
                    "<p style='font-size: 16px;'>Your OTP is:</p>" +
                    "<h1 style='color: #ff5722; letter-spacing: 3px;'>" + otp + "</h1>" +
                    "</div>" +
                    "<p style='text-align: center; margin-top: 20px; font-size: 12px; color: #888;'>This is an automated email. Please do not reply.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            return ResponseEntity.ok("Email Sent Successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error in Sending Email: " + e.getMessage());
        }
    }
}
