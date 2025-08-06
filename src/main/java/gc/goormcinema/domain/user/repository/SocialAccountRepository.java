package gc.goormcinema.domain.user.repository;

import gc.goormcinema.domain.user.entity.SocialAccount;
import gc.goormcinema.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    Optional<SocialAccount> findByProviderAndSocialUid(String provider, String socialUid);

    List<SocialAccount> findByUser(User user);

    List<SocialAccount> findByProvider(String provider);

    @Query("SELECT sa FROM SocialAccount sa WHERE sa.user = :user AND sa.provider = :provider")
    Optional<SocialAccount> findByUserAndProvider(@Param("user") User user, @Param("provider") String provider);

    boolean existsByProviderAndSocialUid(String provider, String socialUid);

    @Query("SELECT COUNT(sa) FROM SocialAccount sa WHERE sa.provider = :provider")
    Long countByProvider(@Param("provider") String provider);
}