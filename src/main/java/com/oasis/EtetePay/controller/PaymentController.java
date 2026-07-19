package com.oasis.EtetePay.controller;

import com.oasis.EtetePay.dto.MerchantPaymentRequest;
import com.oasis.EtetePay.dto.MerchantPaymentResponse;
import com.oasis.EtetePay.service.MerchantPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin()
public class PaymentController {
    private final MerchantPaymentService merchantPaymentService;
    @PostMapping("/user/make-payment")
    ResponseEntity<MerchantPaymentResponse> makePayment(@RequestBody MerchantPaymentRequest merchantPaymentRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(merchantPaymentService.makePayment(merchantPaymentRequest));
    }
}
