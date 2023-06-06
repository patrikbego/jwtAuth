package com.base.jwtAuth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate() {
        // You can return a success message or a JWT token, depending on your requirements
        String message = "Authentication successful";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secureEndpoint() {
        // This endpoint can only be accessed by authenticated users
        String message = "This is a secured endpoint";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/unsecure")
    public ResponseEntity<String> unsecureEndpoint() {
        // This endpoint can be accessed by any user
        String message = "This is a unsecure endpoint";
        return ResponseEntity.ok(message);
    }
}

