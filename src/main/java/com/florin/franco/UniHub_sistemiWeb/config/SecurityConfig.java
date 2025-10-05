package com.florin.franco.UniHub_sistemiWeb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.florin.franco.UniHub_sistemiWeb.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Configurazione principale della sicurezza.
     * - Disabilita CSRF (perché stai costruendo API REST)
     * - Permette l'accesso libero a /api/auth/**
     * - Richiede autenticazione per tutto il resto
     * - Usa il form di login standard (Spring)
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers("/", "/api/auth/**").permitAll()  // ✅ /register e /login liberi
	                    .anyRequest().authenticated()                      // il resto richiede login
	            )
	            .formLogin(form -> form.disable())   // ❌ disabilita login HTML predefinito
	            .httpBasic(basic -> basic.disable()) // ❌ disabilita Basic Auth
	            .build();
	}
    /**
     * PasswordEncoder: serve per confrontare e cifrare le password in modo sicuro.
     * Usa BCrypt (standard consigliato).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Espone il bean AuthenticationManager,
     * utile se in futuro vuoi fare login manuale via endpoint (es. POST /api/login)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}