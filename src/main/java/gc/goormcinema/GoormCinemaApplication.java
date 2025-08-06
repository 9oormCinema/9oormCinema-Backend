package gc.goormcinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoormCinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoormCinemaApplication.class, args);
    }

}
