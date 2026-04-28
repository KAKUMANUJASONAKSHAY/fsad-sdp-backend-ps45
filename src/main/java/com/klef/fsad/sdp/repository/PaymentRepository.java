package com.klef.fsad.sdp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.sdp.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>
{
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
