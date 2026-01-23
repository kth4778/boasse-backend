package hello.boassebackend.domain.auth.service;

import hello.boassebackend.domain.auth.dto.AuthResponse;
import hello.boassebackend.domain.auth.dto.LoginRequest;
import hello.boassebackend.global.exception.LoginFailedException;
import hello.boassebackend.global.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.admin.password}")
    private String adminPassword;

    public AuthResponse login(LoginRequest request) {
        if (!adminPassword.equals(request.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken("admin");

        return AuthResponse.builder()
                .success(true)
                .data(AuthResponse.TokenData.builder()
                        .accessToken(accessToken)
                        .build())
                .build();
    }
}
