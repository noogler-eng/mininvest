package com.minivest.mutualfunds.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.Refund;

public interface RazorpayService {
    public String createCustomer(String name, String email, String phone);
    public Order createOrder(Long amount, String receipt, String notes);

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature);
    public boolean verifyWebhookSignature(String payload, String signature);

    public Payment fetchPayment(String paymentId);

    public Refund createRefund(String paymentId, long amount, String reason);
}
