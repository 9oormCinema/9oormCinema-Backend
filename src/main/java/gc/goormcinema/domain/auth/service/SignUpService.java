package gc.goormcinema.domain.auth.service;

import gc.goormcinema.domain.auth.dto.signUpDTO;
import gc.goormcinema.domain.auth.jwt.service.JwtService;
import gc.goormcinema.domain.user.dto.DuplicationDTO;
import gc.goormcinema.domain.user.entity.UserRole;
import gc.goormcinema.domain.user.entity.User;

import gc.goormcinema.domain.user.error.UserAlreadyExistException;
import gc.goormcinema.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public signUpDTO.TokenResponse signUpBasic(signUpDTO.BasicSignUpRequest request) {

        // 회원가입 전 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException(); // 이미 존재하면 예외 발생
        }

        String accessToken = jwtService.createAccessToken(request.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .phone(request.getPhone())
                .role(UserRole.USER)
                .refreshToken(refreshToken)
                .build();

        user.passwordEncode(passwordEncoder);

        log.info("회원가입 성공");
        userRepository.save(user);

        return signUpDTO.TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public DuplicationDTO.DuplicateEmailResponse checkDuplicateEmail(String email) {
        return new DuplicationDTO.DuplicateEmailResponse(userRepository.existsByEmail(email));
    }
}
