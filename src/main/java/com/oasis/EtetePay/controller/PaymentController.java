package com.oasis.EtetePay.controller;

import com.oasis.EtetePay.dto.PaymentRequest;
import com.oasis.EtetePay.dto.PaymentResponse;
import com.oasis.EtetePay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin()
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/user/make-payment")
    ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.makePayment(paymentRequest));
    }
}
