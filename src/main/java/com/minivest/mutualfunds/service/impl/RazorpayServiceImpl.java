package com.minivest.mutualfunds.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minivest.mutualfunds.service.RazorpayService;
import com.razorpay.Customer;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {
    private final RazorpayClient razorpayClient;

    @Value("$razorpay.key-secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.webhook-secret}")
    private String razorpayWebhookSecret;

    public String createCustomer(String name, String email, String phone){
        try{
            JSONObject customerRequest = new JSONObject();
            customerRequest.put("name", name);
            customerRequest.put("email", email);
            customerRequest.put("contact", phone);

            // API call: POST https://api.razorpay.com/v1/customers
            Customer customer = razorpayClient.customers.create(customerRequest);
            String customerId = customer.get("id");
            log.info("Customer created in Razorpay with ID: {}", customerId);
            return customerId;
        }catch(RazorpayException e){
            log.error("Error creating customer in Razorpay: {}", e.getMessage());
            throw new RuntimeException("Failed to create customer in Razorpay");
        }
    }

    public Order createOrder(Long amount, String receipt, String notes){
        try{
            JSONObject orderRequest = new JSONObject();
            // converting amount to paise (1 INR = 100 paise)
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);

            if(notes != null){
                orderRequest.put("notes", notes);
            }

            // creating order in Razorpay, now razorpay will generate a unique order ID for this order
            // we send checkout options to frontend along with this order ID, and when user completes payment, R
            // azorpay will send us the order ID and payment ID in the callback or webhook, 
            // we can use those IDs to verify the payment and update our records accordingly
            Order order = razorpayClient.orders.create(orderRequest);
            log.info("Order created in Razorpay with receipt: {} and order id: {}", receipt, order.get("id"));
            return order;
        }catch(RazorpayException e){
            log.error("Error creating order in Razorpay: {}", e.getMessage());
            throw new RuntimeException("Failed to create order in Razorpay");
        }
    }


    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature){
        try{
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            
            return com.razorpay.Utils.verifyPaymentSignature(attributes, signature);
        }catch(RazorpayException e){
            log.error("Error verifying payment signature: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }

    public boolean verifyWebhookSignature(String payload, String signature){
        try{
            // Razorpay's webhook signature verification requires the raw payload 
            // and the signature sent in the header
            return com.razorpay.Utils.verifyWebhookSignature(payload, signature, razorpayWebhookSecret);
        }catch(RazorpayException e){
            log.error("Error verifying webhook signature: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }

    public Payment fetchPayment(String paymentId){
        try{
            Payment payment = razorpayClient.payments.fetch(paymentId);
            log.info("Payment fetched from Razorpay with ID: {}", paymentId);
            return payment;
        }catch(RazorpayException e){
            log.error("Error fetching payment from Razorpay: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch payment from Razorpay");
        }
    }

    public Refund createRefund(String paymentId, long amount, String reason){
        try{
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", amount * 100);
            // Razorpay allows three speeds for refunds: "normal", "optimum", and "instant". 
            // "optimum" is the default and usually takes 2-3 business days.
            refundRequest.put("speed", "optimum");
            refundRequest.put("notes", new JSONObject().put("reason", reason));

            Refund refund = razorpayClient.payments.refund(paymentId, refundRequest);
            log.info("Refund created in Razorpay for payment ID: {}", paymentId);
            return refund;
        }catch(RazorpayException e){
            log.error("Error creating refund in Razorpay: {}", e.getMessage());
            throw new RuntimeException("Failed to create refund in Razorpay");
        }
    }
}
