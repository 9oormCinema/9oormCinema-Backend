package gc.goormcinema.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        // POST /events/{eventId}/reviews 경로에만 CSRF 검사 제외
                        .ignoringRequestMatchers("/events/*/reviews")
                )
                .authorizeHttpRequests(auth -> auth
                        // 나머지 엔드포인트는 모두 허용 (필요에 따라 변경)
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
