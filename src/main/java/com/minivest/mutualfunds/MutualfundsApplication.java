// Every Java file starts with its package declaration.
// Package = folder path: com/minivest/ on disk.
package com.minivest.mutualfunds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// THE magic annotation. Combines 3 things:
// @Configuration — this class can define @Bean methods
// @EnableAutoConfiguration — Spring auto-configures based on dependencies
// @ComponentScan — scans com.minivest.* for @Service, @Controller, @Repository, etc.

@EnableScheduling
// Enables @Scheduled annotation — we need this for the SIP cron job.
// Without this, @Scheduled methods won't execute.
@EnableRetry
// Enable retry in your main application class
public class MutualfundsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MutualfundsApplication.class, args);
		// Starts the entire Spring Boot application:
        // Creates Spring IoC container (ApplicationContext)
        // Scans for beans (@Service, @Controller, @Repository, @Configuration)
        // Auto-configures (database, web server, JPA, etc.)
        // Starts embedded Tomcat on port 8080
        // Application is now ready to receive HTTP requests!
	}
}
