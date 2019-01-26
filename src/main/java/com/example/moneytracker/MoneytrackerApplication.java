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
				userRepo.save(new User("Khidr", "khidr", encryptPassword("khidr")));
				userRepo.save(new User("Bimo", "bimo", encryptPassword("bimo")));
			}
		};
	}

	private String encryptPassword(String password) throws Exception {
		byte[] bytes = password.getBytes("UTF-8");
		return DigestUtils.md5DigestAsHex(bytes);
	}

}

