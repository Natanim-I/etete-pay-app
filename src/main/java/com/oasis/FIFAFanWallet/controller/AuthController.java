package com.oasis.FIFAFanWallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<String> login() {
        String token = null;
        return ResponseEntity.ok(token);
    }
}
