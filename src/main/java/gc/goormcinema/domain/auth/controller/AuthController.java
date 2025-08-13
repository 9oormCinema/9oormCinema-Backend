package gc.goormcinema.domain.auth.controller;

import gc.goormcinema.domain.auth.dto.LoginDTO;
import gc.goormcinema.domain.auth.dto.signUpDTO;
import gc.goormcinema.domain.auth.email.service.MailService;
import gc.goormcinema.domain.auth.jwt.service.JwtService;
import gc.goormcinema.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth", description = "회원가입 및 로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@Slf4j
public class AuthController {
    private final JwtService jwtService;
    private final MailService mailService;


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<signUpDTO.TokenResponse> login(@RequestBody LoginDTO.LoginRequest request) {

        return ResponseEntity.ok().body(new signUpDTO.TokenResponse());
    }

    @Operation(summary = "리프레쉬 토큰을 통한 재발급", description = "토큰을 재발급 합니다")
    @PostMapping("/refresh")
    public ResponseEntity<signUpDTO.TokenResponse> refresh(@RequestBody signUpDTO.RefreshRequest request) {
        signUpDTO.TokenResponse response = jwtService.checkRefreshTokenAndReIssueAccessToken(request.getRefreshToken());

        return ResponseEntity.ok().body(response);
    }
}
