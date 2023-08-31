package com.example.stomp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService users() {
        UserDetails user1 = User.builder()
                .username("user1")
                .password("{noop}pass1")
                .roles("role1")
                .build();
        UserDetails user2 = User.builder()
                .username("user2")
                .password("{noop}pass2")
                .roles("role2")
                .build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
}
