package wad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org...jpa")
public class TypeYourWordsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TypeYourWordsApplication.class, args);
    }
}
