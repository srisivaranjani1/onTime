//package com.example.onTime.controllers;
//
//import com.example.onTime.dto.LoginRequest;
//import com.example.onTime.models.User;
//import com.example.onTime.repository.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
//        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
//
//        if (optionalUser.isEmpty()) {
//            return ResponseEntity.status(404).body("User not found");
//        }
//
//        User user = optionalUser.get();
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid password");
//        }
//
//
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // role must start with ROLE_
//        );
//
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authToken);
//        SecurityContextHolder.setContext(context);
//
//
//        HttpSessionSecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
//        contextRepository.saveContext(context, new ServletServerHttpRequest(request).getServletRequest(), null);
//
//
//        request.getSession().setAttribute("userId", user.getId());
//        System.out.println("Session userId: " + request.getSession().getAttribute("userId"));
//
//        return ResponseEntity.ok("Login successful");
//    }
//
//
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
//        if (existingUser.isPresent()) {
//            return ResponseEntity.badRequest().body("Email already in use");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//}
//
//



package com.example.onTime.controllers;

import com.example.onTime.dto.LoginRequest;
import com.example.onTime.models.User;
import com.example.onTime.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityContextRepository securityContextRepository;


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Find user by email
        Optional<User> optionalUser =
                userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("User not found");
        }

        User user = optionalUser.get();


        // Check password
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid password");
        }


        // Create Spring Security UserDetails
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );


        // Create Authentication object
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );


        // Create SecurityContext
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authToken);


        // Put authentication into SecurityContextHolder
        SecurityContextHolder.setContext(context);


        // Save SecurityContext into HTTP session
        securityContextRepository.saveContext(
                context,
                request,
                response
        );


        // Store our application's user ID in session
        request.getSession().setAttribute(
                "userId",
                user.getId()
        );


        // Debug logs
        System.out.println(
                "Session userId: "
                        + request.getSession().getAttribute("userId")
        );

        System.out.println(
                "Authenticated user: "
                        + SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        );

        System.out.println(
                "Authorities: "
                        + SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
        );


        return ResponseEntity
                .ok("Login successful");
    }



    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody User user) {

        // Check whether email already exists
        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already in use");
        }


        // Encrypt password using BCrypt
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );


        // Save user
        userRepository.save(user);


        return ResponseEntity
                .ok("User registered successfully");
    }
}