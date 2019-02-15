package com.example.moneytracker;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
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
				userRepo.save(new User("Khidr", "khidr", encryptPassword("k_2121!")));
				userRepo.save(new User("Bimo", "bimo", encryptPassword("b_2121!")));
			}
		};
	}

	///////////////////////////////// Shiro /////////////////////////////
	@Autowired
	private UserRepo userRepo;

	@Bean
	public Realm realm() {
		TextConfigurationRealm realm = new TextConfigurationRealm();
//		realm.setUserDefinitions("joe.coder=password,user\n" +
//				"jill.coder=password,admin");
		realm.setUserDefinitions("khidr=k_2121!,user\n" +
				"bimo=b_2121!,admin");
//		addUsersToRealm(realm);
		realm.setRoleDefinitions("admin=read,write\n" +
				"user=read");
		realm.setCachingEnabled(true);
		return realm;
	}

	private void addUsersToRealm(TextConfigurationRealm realm) {
		Iterable<User> users = userRepo.findAll();
		String def = "";
		for (User user : users) {
			def += user.getUsername() + "=" + user.getPassword() + ",admin\n";
		}
		realm.setUserDefinitions(def);
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		chainDefinition.addPathDefinition("/login.html", "authc"); // need to accept POSTs from the login form
		chainDefinition.addPathDefinition("/logout", "logout");
		return chainDefinition;
	}
//	@ModelAttribute(name = "subject")
//	public Subject subject() {
//		return SecurityUtils.getSubject();
//	}
	//////////////////////////////////////////////////////////////

	private String encryptPassword(String password) throws Exception {
		byte[] bytes = password.getBytes("UTF-8");
		return DigestUtils.md5DigestAsHex(bytes);
	}

}

