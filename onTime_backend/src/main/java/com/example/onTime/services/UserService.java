package com.example.onTime.services;

import com.example.onTime.models.User;
import com.example.onTime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public String createUser(User user) {
        userRepository.save(user);
        return "User created successfully";
    }

    public String updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            if (updatedUser.getName() != null) existing.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null) existing.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null) existing.setPassword(updatedUser.getPassword());
            userRepository.save(existing);
            return "User updated successfully";
        }
        return "User not found";
    }

    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
