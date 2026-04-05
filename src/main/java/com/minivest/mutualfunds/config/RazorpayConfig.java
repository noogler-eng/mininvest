package com.minivest.mutualfunds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
// Tells Spring: "this class provides bean definitions."
// Spring processes @Configuration classes at startup to create beans.
public class RazorpayConfig {
    // @Value injects a value from application.yml.
    @Value("${razorpay.key-id}")
    private String keyId;
    @Value("${razorpay.key-secret}")
    private String keySecret;

    @Bean
    // @Bean tells Spring: "the object returned by this method should be
    // managed by Spring's IoC container as a singleton."
    // Anywhere you @Autowired RazorpayClient, you get THIS instance.
    // Singleton = ONE instance shared across entire app (thread-safe for Razorpay client).
    public RazorpayClient razorpayClient() throws RazorpayException {
        // Creates and returns a RazorpayClient bean using the injected properties.
        return new RazorpayClient(keyId, keySecret);
    }
}
