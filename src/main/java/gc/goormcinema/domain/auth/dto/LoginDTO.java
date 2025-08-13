package gc.goormcinema.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

public class LoginDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Jacksonized
    @Builder
    public static class PasswordUpdateRequest {
        private String password;
    }
}
