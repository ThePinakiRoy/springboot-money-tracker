package com.codewithroy.resttester.expense.web.controller;

import com.codewithroy.resttester.expense.web.dto.AuthRequest;
import com.codewithroy.resttester.expense.service.AuthService;
import com.codewithroy.resttester.expense.web.dto.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        authService.register(authRequest);
        return new ResponseEntity<String>("User registration successful", HttpStatus.OK);
    }

    @GetMapping("/verify-account/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token){
       boolean flag = authService.verifyAccount(token);
       if (flag) {
           return new ResponseEntity<>("Account Activated Successfully!!", HttpStatus.OK);
       } else {
           return new ResponseEntity<>("Failed to Activate Account", HttpStatus.NOT_FOUND);
       }
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }
}
