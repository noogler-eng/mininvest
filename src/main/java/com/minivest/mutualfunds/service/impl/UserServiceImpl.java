package com.minivest.mutualfunds.service.impl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
    public void createCustomer(String name, String email, String phone){

    }
    
    public void createOrder(Long amount, String receipt, JSONObject notes){

    }
    
    public void getUserPendingAction(){

    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature){
        return Boolean.FALSE;
    }
    
    public void verifyWebhookSignature(String payload, String signature){

    }
    
    public void fetchPayment(String paymentId){

    }

    public void createRefund(String paymentId, Long amount, String reason){

    }
}
