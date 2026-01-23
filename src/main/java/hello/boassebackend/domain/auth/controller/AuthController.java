package hello.boassebackend.domain.auth.controller;

import hello.boassebackend.domain.auth.dto.AuthResponse;
import hello.boassebackend.domain.auth.dto.LoginRequest;
import hello.boassebackend.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify() {
        // 인터셉터를 통과했다면 토큰이 유효한 것임
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
