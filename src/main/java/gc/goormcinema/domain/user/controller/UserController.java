package gc.goormcinema.domain.user.controller;


import gc.goormcinema.domain.auth.PrincipalDetails;
import gc.goormcinema.domain.user.dto.UserDTO;
import gc.goormcinema.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "마이페이지 조회")
    @GetMapping("/me")
    public ResponseEntity<UserDTO.UserInfoResponse> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("[USER]: {} 님 마이페이지 조회", principalDetails.getId());

        return ResponseEntity.ok(userService.getUserInfo(principalDetails.getId()));
    }

    @Operation(summary = "마이페이지 수정")
    @PatchMapping(path = "/me")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UserDTO.UserUpdateRequest request) {
        log.info("[USER] : {} 님 마이페이지 정보 수정", principalDetails.getId());

        userService.updateUser(principalDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
