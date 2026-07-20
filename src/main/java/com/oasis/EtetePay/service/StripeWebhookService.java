package com.oasis.EtetePay.service;

import com.oasis.EtetePay.exception.StripePaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final ExternalPaymentService externalPaymentService;

    public void handleStripeWebhook(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
             throw new StripePaymentException("Webhook signature verification failed.");
        }

        switch (event.getType()) {
            case "payment_intent.succeeded" ->
                externalPaymentService.handlePaymentSuccess(event);

            case "payment_intent.payment_failed" ->
                externalPaymentService.handlePaymentFailure(event);

            default -> 
              log.warn("Unhandled event type: {}", event.getType());
        }
    }
}
