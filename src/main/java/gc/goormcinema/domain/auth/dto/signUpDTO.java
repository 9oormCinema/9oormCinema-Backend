package gc.goormcinema.domain.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

public class signUpDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BasicSignUpRequest {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String name;
        @NotBlank
        private String phone;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OauthSignUpRequest {
        @NotBlank
        private String name;
        @NotBlank
        private int age;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpResponse {
        private TokenResponse tokenResponse;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;

        @JsonIgnore
        private final ObjectMapper objectMapper = new ObjectMapper();

        public String convertToJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(this);
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class RefreshRequest {
        private String refreshToken;
    }
}
