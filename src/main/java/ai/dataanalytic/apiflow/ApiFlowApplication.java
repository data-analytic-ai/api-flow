package ai.dataanalytic.apiflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

/**
 * Main entry point for the APIFlow application.
 * Bootstraps the Spring Boot WebFlux application.
 */
@Slf4j
@SpringBootApplication
public class ApiFlowApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiFlowApplication.class, args);
		log.info("APIFlow Application Started Successfully!");
	}
}
