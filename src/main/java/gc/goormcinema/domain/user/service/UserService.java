package gc.goormcinema.domain.user.service;


import gc.goormcinema.domain.user.dto.UserDTO;
import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.repository.UserRepository;
import gc.goormcinema.domain.user.error.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserDTO.UserInfoResponse getUserInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserDTO.UserInfoResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .userRole(user.getRole())
                .build();
    }

    public void updateUser(Long userId, UserDTO.UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.update(request);

        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public void updatePassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setPassword(password);
        //user.passwordEncode(passwordEncoder);

        userRepository.save(user);
    }
}
