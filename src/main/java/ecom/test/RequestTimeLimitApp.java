package ecom.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.NoArgsConstructor;

@SpringBootApplication
@EnableScheduling
@NoArgsConstructor
public class RequestTimeLimitApp {

	public static void main(String[] args) {
		SpringApplication.run(RequestTimeLimitApp.class, args);
	}

}
