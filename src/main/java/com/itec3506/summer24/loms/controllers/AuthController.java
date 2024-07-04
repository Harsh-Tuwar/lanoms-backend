package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.AuthRequest;
import com.itec3506.summer24.loms.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController // This means that this class is a Controller
@RequestMapping("/auth") // This means URL's start with /demo (after Application path)
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        HashMap<String, Object> resp = new HashMap<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                resp.put("token", jwtService.generateToken(authRequest.getUsername()));
            } else {
                resp.put("error", "Invalid user request");
            }

            return ResponseEntity.ok(resp);
        } catch (AuthenticationException error) {
            resp.put("error", error.getMessage());
            return ResponseEntity.ofNullable(resp);
        }
    }

    @ResponseBody
    @PostMapping("/ping")
    public ResponseEntity<HashMap<String, Object>> checkAuthToken() {
        HashMap<String, Object> resp = new HashMap<>();

        try {
            resp.put("message", "ok!");
            return ResponseEntity.ok(resp);
        } catch (AuthenticationException error) {
            resp.put("error", error.getMessage());
            return ResponseEntity.ofNullable(resp);
        }
    }
}
