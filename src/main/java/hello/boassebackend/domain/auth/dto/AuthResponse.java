package hello.boassebackend.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private boolean success;
    private TokenData data;

    @Getter
    @Builder
    public static class TokenData {
        private String accessToken;
    }
}
