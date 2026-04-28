package com.klef.fsad.sdp.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.entity.Payment;
import com.klef.fsad.sdp.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@RestController
@RequestMapping("/payment")
public class PaymentController
{
	@Value("${razorpay.key.id}")
	private String keyId;

	@Value("${razorpay.key.secret}")
	private String keySecret;


    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/createorder")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request)
    {
        try
        {
            if (keyId == null || keyId.isBlank() || keySecret == null || keySecret.isBlank())
            {
                return ResponseEntity.status(500).body("Razorpay credentials are not configured");
            }

            RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount() * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", request.getReceipt());

            Order order = razorpayClient.orders.create(orderRequest);

            Payment payment = new Payment();
            payment.setRazorpayOrderId(order.get("id"));
            payment.setAmount(request.getAmount());
            payment.setCurrency("INR");
            payment.setReceipt(request.getReceipt());
            payment.setStatus("CREATED");
            payment.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            return ResponseEntity.ok(
                    Map.of(
                            "key", keyId,
                            "orderId", order.get("id"),
                            "amount", request.getAmount() * 100,
                            "currency", "INR"
                    )
            );
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Order Creation Failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerifyRequest request)
    {
        try
        {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new RuntimeException("Payment Order Not Found"));

            if (isValid)
            {
                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
                payment.setStatus("SUCCESS");
                paymentRepository.save(payment);
                return ResponseEntity.ok("Payment Verified Successfully");
            }

            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return ResponseEntity.status(400).body("Payment Verification Failed");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Payment Verification Error: " + e.getMessage());
        }
    }

    static class PaymentRequest
    {
        private int amount;
        private String receipt;

        public int getAmount()
        {
            return amount;
        }

        public void setAmount(int amount)
        {
            this.amount = amount;
        }

        public String getReceipt()
        {
            return receipt;
        }

        public void setReceipt(String receipt)
        {
            this.receipt = receipt;
        }
    }

    static class PaymentVerifyRequest
    {
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private String razorpaySignature;

        public String getRazorpayOrderId()
        {
            return razorpayOrderId;
        }

        public void setRazorpayOrderId(String razorpayOrderId)
        {
            this.razorpayOrderId = razorpayOrderId;
        }

        public String getRazorpayPaymentId()
        {
            return razorpayPaymentId;
        }

        public void setRazorpayPaymentId(String razorpayPaymentId)
        {
            this.razorpayPaymentId = razorpayPaymentId;
        }

        public String getRazorpaySignature()
        {
            return razorpaySignature;
        }

        public void setRazorpaySignature(String razorpaySignature)
        {
            this.razorpaySignature = razorpaySignature;
        }
    }
}
