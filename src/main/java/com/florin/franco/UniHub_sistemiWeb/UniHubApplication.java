package com.florin.franco.UniHub_sistemiWeb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@SpringBootApplication
public class UniHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniHubApplication.class, args);
	}
	
	@Bean
    CommandLineRunner initDatabase(AppUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                AppUser user = new AppUser(
                        null,
                        "admin",
                        encoder.encode("1234"),
                        Ruolo.SUPERADMIN,
                        "admin@unihub.com"
                );
                repo.save(user);
                System.out.println("✅ Utente admin creato (password: 1234)");
            }
        };
	 }
	
	}

