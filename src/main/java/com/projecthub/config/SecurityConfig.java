package com.projecthub.config;

import com.projecthub.repositories.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/browser", "/login", "/register", "/css/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage("/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/browser", false)
            )
            .logout(logout -> logout.logoutSuccessUrl("/?logout"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService accountDetailsService(AccountRepository accounts) {
        return email -> accounts.findByEmail(email)
                .map(account -> User.withUsername(account.getEmail())
                        .password(account.getHashedPassword())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
