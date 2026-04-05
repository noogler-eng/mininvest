package com.minivest.mutualfunds.webhook;

public class WebhookProcessor {
    



    // From Razorpay webhook to RabbitMQ to BSE order to notification.
    // paymentCaptured when razorpay captures the payment. 
    // This is the event we care about to update our transaction 
    // status to SUCCESS.
    public void handlePaymentCaptured(){
        // 1. we have to publish an event
        // build an event
        // builder pattern is used to create complex objects with many parameters in a readable way. 
        // It allows you to set only the fields you care about and provides a 
        // fluent API for object creation.
        // OrderEvent event = OrderEvent.builder()
        //     .eventType("payment_captured")
        //     .razorpayOrderId("order_123")
        //     .build();

        // Both consumers run INDEPENDENTLY and CONCURRENTLY.
        // If email service is down, MF order still processes!
        // If BSE is down, notification still sends!
        // This is the POWER of event-driven architecture.

        // 2. Publish to both queues
        // MF order consumer picks up → places order on BSE
        // orderPublisher.publishMfOrderEvent(event);
        // Notification consumer picks up → sends SMS + email
        // orderPublisher.publishNotificationEvent(event);
    }
}
