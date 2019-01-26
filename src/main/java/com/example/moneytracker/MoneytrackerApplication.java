package com.example.moneytracker;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.DigestUtils;

@SpringBootApplication
public class MoneytrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneytrackerApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(UserRepo userRepo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				byte[] password = "khidr".getBytes("UTF-8");
				String passwordEncypted =  DigestUtils.md5DigestAsHex(password);
				User user = new User("Khidr", "khidr", passwordEncypted);
				userRepo.save(user);
			}
		};
	}

}

