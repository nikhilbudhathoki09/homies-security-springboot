package homiessecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecurityHomiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityHomiesApplication.class, args);

	}

}
