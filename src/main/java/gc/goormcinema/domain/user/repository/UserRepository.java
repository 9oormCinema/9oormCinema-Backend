package gc.goormcinema.domain.user.repository;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<User> findByRole(UserRole role);

    List<User> findByIsSocial(Boolean isSocial);

    Optional<User> findByRefreshToken(String refreshToken);

}
