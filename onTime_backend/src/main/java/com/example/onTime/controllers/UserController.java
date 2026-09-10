//package com.example.onTime.controllers;
//
//import com.example.onTime.models.User;
//import com.example.onTime.repository.UserRepository;
//import com.example.onTime.services.UserService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//     private UserRepository userRepository;
//    @PostMapping
//    public String createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public User getUser(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    @PutMapping("/{id}")
//    public String updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        return userService.updateUser(id, updatedUser);
//    }
//
//    @DeleteMapping("/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        return userService.deleteUser(id);
//    }
//
//
//    @GetMapping("/me")
//    public ResponseEntity<User> getCurrentUser(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Optional<User> optionalUser = userRepository.findById(userId);
//        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//
//}
//
//
//



package com.example.onTime.controllers;

import com.example.onTime.models.User;
import com.example.onTime.repository.UserRepository;
import com.example.onTime.services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://on-time-theta.vercel.app"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @PostMapping
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }



    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }



    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }



    @PutMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        return userService.updateUser(id, updatedUser);
    }



    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }



    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        System.out.println(
                "UserController /me - Session userId: "
                        + userId
        );

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                optionalUser.get()
        );
    }
}
