package gc.goormcinema.domain.user.dto;

import gc.goormcinema.domain.user.entity.UserRole;
import gc.goormcinema.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfoResponse {
        private Long id;
        private String email;
        private String name;
        private String phone;
        private UserRole userRole;



        public static UserInfoResponse toDTO(User user) {
            return new UserInfoResponse(user.getId(), user.getEmail(),
                    user.getName(), user.getPhone(),
                    user.getRole());
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserUpdateRequest {
        private String name;
        private String phone;
    }
}
