package br.gov.mt.seplag.api.controller;

import br.gov.mt.seplag.api.dto.LoginRequest;
import br.gov.mt.seplag.api.dto.LoginResponse;
import br.gov.mt.seplag.api.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        
        // O HASH QUE O SEU COMPUTADOR GEROU 
        String hashedPass = "$2a$10$W.N9IUHbjGn2/cvS4hIeROF3BIObw8B52QSZQEJkS.4sSXThYETq2";

        System.out.println(">>> Tentativa de login: " + request.getUsername());
        
        // Compara a senha digitada com o seu hash local
        boolean isMatch = passwordEncoder.matches(request.getPassword(), hashedPass);
        System.out.println(">>> A senha bate? " + (isMatch ? "SIM" : "NAO"));

        if ("admin".equals(request.getUsername()) && isMatch) {
            String token = jwtService.generateToken(request.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        
        return ResponseEntity.status(401).build();
    }
}