package in.sp.main;

import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class EducationCrmProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducationCrmProjectApplication.class, args);
	}
}

