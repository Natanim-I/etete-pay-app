package com.oasis.EtetePay.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalPaymentConfig {
    @Value("${stripe.secret.key}")
    private String stripeKey;

    @PostConstruct
    public void initKey(){
        Stripe.apiKey = stripeKey;
    }
}
