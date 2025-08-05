package gc.goormcinema.domain.user.entity;

import gc.goormcinema.domain.user.dto.UserDTO;
import gc.goormcinema.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;

    @Column(name = "is_social", nullable = false)
    private Boolean isSocial = false;

    private String refreshToken;


    @Builder
    public User(String email, String password,
                String name, String phone, UserRole role,
                Boolean isSocial,
                String refreshToken) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role != null ? role : UserRole.USER;
        this.isSocial = isSocial != null ? isSocial : false;
        this.refreshToken = refreshToken;
    }

    //pw 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public User update(UserDTO.UserUpdateRequest user) {
        this.name = user.getName();
        this.phone = user.getPhone();

        return this;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public boolean isSocialUser() {
        return this.isSocial;
    }
}