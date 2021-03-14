package com.kjk.mountplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
@EnableMongoRepositories(basePackages = "com.kjk.mountplanner.repositories")
public class MountPlannerJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MountPlannerJavaApplication.class, args);
		
	}

}
