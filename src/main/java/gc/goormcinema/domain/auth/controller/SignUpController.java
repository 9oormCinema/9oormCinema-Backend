package gc.goormcinema.domain.auth.controller;

import gc.goormcinema.domain.auth.PrincipalDetails;
import gc.goormcinema.domain.auth.dto.signUpDTO;
import gc.goormcinema.domain.auth.email.dto.EmailDTO;
import gc.goormcinema.domain.auth.email.service.MailService;
import gc.goormcinema.domain.auth.email.service.MailVerifyService;
import gc.goormcinema.domain.auth.service.SignUpService;
import gc.goormcinema.domain.user.dto.DuplicationDTO;
import gc.goormcinema.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Tag(name = "SignUp", description = "회원가입 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@ResponseBody
public class SignUpController {
    private final SignUpService signUpService;
    private final MailService mailService;
    private final MailVerifyService mailVerifyService;


    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<signUpDTO.TokenResponse> SignUpBasic(@Valid @RequestBody signUpDTO.BasicSignUpRequest request) {
        signUpDTO.TokenResponse response = signUpService.signUpBasic(request);
        log.info("[AUTH] {}님 기본 회원가입 완료", request.getEmail());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이메일 중복 체크")
    @GetMapping("/check/email/{email}")
    public ResponseEntity<DuplicationDTO.DuplicateEmailResponse> checkDuplicateEmail(@PathVariable String email) {
        DuplicationDTO.DuplicateEmailResponse response = signUpService.checkDuplicateEmail(email);
        log.info("[AUTH] {} 닉네임 중복 체크 완료", email);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원가입 인증 메일 발송", description = "회원가입 인증 메일을 발송합니다")
    @GetMapping("/email/{email}")
    public ResponseEntity<EmailDTO.EmailSendResponse> sendSignUpAuthEmail(@PathVariable String email) {
        mailService.sendSignUpEmail(email);
        log.info("[EMAIL] {}님 회원가입 인증 메일 발송 완료", email);

        return ResponseEntity.ok().body(null);
    }



    @Operation(summary = "회원가입 인증 메일 검증", description = "회원가입 인증 메일을 검증합니다")
    @PostMapping("/email")
    public ResponseEntity<EmailDTO.SignUpEmailVerifyResponse> verifySingUpAuthEmail(@RequestBody EmailDTO.EmailVerifyRequest request) {
        EmailDTO.SignUpEmailVerifyResponse response = mailVerifyService.CheckSignUpAuthNum(request.getEmail(),
                request.getAuthNum());
        log.info("[EMAIL] {}님 회원가입 인증 메일 검증 완료", request.getEmail());
        return ResponseEntity.ok().body(response);
    }
}