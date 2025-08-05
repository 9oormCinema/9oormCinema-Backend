package gc.goormcinema.domain.user.entity;

import gc.goormcinema.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "social_uid", nullable = false)
    private String socialUid;

    @Builder
    public SocialAccount(User user, String provider, String socialUid) {
        this.user = user;
        this.provider = provider;
        this.socialUid = socialUid;
    }

    public void updateSocialUid(String socialUid) {
        this.socialUid = socialUid;
    }
}