package com.booknest.booknest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/book/**",
                    "/search",
                    "/ai-recommend",
                    "/css/**",
                    "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
            );
        return http.build();
    }
}