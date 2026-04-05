package com.minivest.mutualfunds.service;

import org.json.JSONObject;

public interface UserService {
    public void createCustomer(String name, String email, String phone);
    public void createOrder(Long amount, String receipt, JSONObject notes);
    
    public void getUserPendingAction();

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature); 
    public void verifyWebhookSignature(String payload, String signature);
    
    public void fetchPayment(String paymentId);

    public void createRefund(String paymentId, Long amount, String reason);
}
