package com.klef.fsad.sdp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_table")
@Data
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private int amount;

    private String currency;

    private String status;

    private String receipt;

    private LocalDateTime createdAt;
}
