package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{
	@Autowired
	private AppUserRepository repository;
	
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        AppUser user = repository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

	        return org.springframework.security.core.userdetails.User
	                .withUsername(user.getUsername())
	                .password(user.getPassword())
	                .roles(user.getRole().name()) // <-- Enum → String ("ADMIN", ecc.)
	                .build();
	    }
}
