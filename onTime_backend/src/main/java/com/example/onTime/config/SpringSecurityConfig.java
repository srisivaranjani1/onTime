//package com.example.onTime.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//
//@Configuration
//public class SpringSecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
//                        .requestMatchers("/api/users/me").authenticated()
//                        .requestMatchers("/api/meetings/create").hasRole("USER")
//                        .requestMatchers("/api/todos/**").authenticated()
//                        .anyRequest().permitAll()
//                )
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(logout -> logout.logoutUrl("/api/users/logout").permitAll())
//                .sessionManagement(session -> session.sessionFixation().migrateSession().maximumSessions(1));
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public SecurityContextRepository securityContextRepository() {
//        return new HttpSessionSecurityContextRepository();
//    }
//
//}


package com.example.onTime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .securityContext(context ->
                        context.securityContextRepository(
                                securityContextRepository
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // Login and registration are public
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register"
                        ).permitAll()
                        // Current logged-in user
                        .requestMatchers(
                                "/api/users/me"
                        ).authenticated()
                        // Create meeting requires USER role
                        .requestMatchers(
                                "/api/meetings/create"
                        ).hasRole("USER")
                        // Todo APIs require authentication
                        .requestMatchers(
                                "/api/todos/**"
                        ).authenticated()
                        // Everything else is currently public
                        .anyRequest().permitAll()
                )

                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/api/users/logout")
                                .permitAll()
                )

                .sessionManagement(session ->
                        session
                                .sessionFixation()
                                .migrateSession()
                                .maximumSessions(1)
                );


        return http.build();
    }


    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================
    // SECURITY CONTEXT REPOSITORY
    // =========================

    @Bean
    public SecurityContextRepository securityContextRepository() {

        return new HttpSessionSecurityContextRepository();
    }
}