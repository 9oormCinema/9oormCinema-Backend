package gc.goormcinema.domain.user.error;

import gc.goormcinema.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() { super(String.valueOf(GlobalErrorStatus.USER_NOT_FOUND)); }
}
