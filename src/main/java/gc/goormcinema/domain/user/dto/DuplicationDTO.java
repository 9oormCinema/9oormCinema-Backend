package gc.goormcinema.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DuplicationDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DuplicateEmailRequest {
        private String email;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DuplicateEmailResponse {
        private boolean duplicate;
    }
}
