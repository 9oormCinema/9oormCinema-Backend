package gc.goormcinema.domain.user.error;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() { super(String.valueOf(ErrorCode.USER_NOT_FOUND)); }
}
