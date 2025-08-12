package gc.goormcinema.domain.user.error;

import gc.goormcinema.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.persistence.EntityNotFoundException;

public class UserAlreadyExistException extends EntityNotFoundException {
    public UserAlreadyExistException() { super(String.valueOf(GlobalErrorStatus.USER_ALREADY_EXIST)); }
}
