package gc.goormcinema.domain.auth.email.service;

import gc.goormcinema.domain.auth.dto.signUpDTO;
import gc.goormcinema.domain.auth.email.dto.EmailDTO;
import gc.goormcinema.domain.auth.jwt.service.JwtService;
import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.error.UserAlreadyExistException;
import gc.goormcinema.domain.user.error.UserNotFoundException;
import gc.goormcinema.domain.user.repository.UserRepository;
import gc.goormcinema.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailVerifyService {
    private final RedisUtil redisUtil;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public EmailDTO.SignUpEmailVerifyResponse CheckSignUpAuthNum(String email, String authNum) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException();
        }

        if (redisUtil.getData(email) == null) {
            return EmailDTO.SignUpEmailVerifyResponse.builder()
                    .verifyResult(false)
                    .email(email)
                    .build();

        } else if (redisUtil.getData(email).equals(authNum)) {
            redisUtil.deleteData(email);
            return EmailDTO.SignUpEmailVerifyResponse.builder()
                    .verifyResult(true)
                    .email(email)
                    .build();
        }

        return EmailDTO.SignUpEmailVerifyResponse.builder()
                .verifyResult(false)
                .email(email)
                .build();
    }

    public EmailDTO.FindPwEmailVerifyResponse CheckFindPwAuthNum(String email, String authNum) {
        User targetUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (redisUtil.getData(email) == null) {
            return EmailDTO.FindPwEmailVerifyResponse.builder()
                    .verifyResult(false)
                    .email(email)
                    .build();
        } else if (redisUtil.getData(email).equals(authNum)) {
            redisUtil.deleteData(email);

            signUpDTO.TokenResponse token = signUpDTO.TokenResponse.builder()
                    .accessToken(jwtService.createAccessToken(targetUser.getEmail()))
                    .refreshToken(jwtService.reIssueRefreshToken(targetUser))
                    .build();

            return EmailDTO.FindPwEmailVerifyResponse.builder()
                    .verifyResult(true)
                    .email(email)
                    .tokenResponse(token)
                    .build();
        }

        return EmailDTO.FindPwEmailVerifyResponse.builder()
                .verifyResult(false)
                .email(email)
                .build();
    }
}