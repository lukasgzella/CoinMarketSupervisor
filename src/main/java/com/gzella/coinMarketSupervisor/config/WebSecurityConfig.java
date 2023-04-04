package com.gzella.coinMarketSupervisor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .antMatchers("/static/**").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/", "/home")
                        .permitAll()
                        .antMatchers("/registration")
                        .permitAll()
                        .antMatchers("/register-test-user")
                        .permitAll()
                        .antMatchers(HttpMethod.POST, "/registeruser")
                        .permitAll()
                        .antMatchers("/registeruser")
                        .permitAll()
                        .antMatchers("/menu/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}