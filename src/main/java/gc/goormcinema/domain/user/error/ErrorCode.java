package gc.goormcinema.domain.user.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //USER
    USER_NOT_FOUND("U02", "User is not Found.",HttpStatus.BAD_REQUEST.value());

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
