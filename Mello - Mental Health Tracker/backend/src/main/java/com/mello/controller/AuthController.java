package com.mello.controller;

import com.mello.model.User;
import com.mello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.mello.service.EmailService;
import com.mello.model.Notification;
import com.mello.repository.NotificationRepository;
import java.util.UUID;
import java.util.Map;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFullName());
            
            // Create in-app welcome notification
            Notification welcomeNotification = new Notification();
            welcomeNotification.setUserId(savedUser.getId());
            welcomeNotification.setType("WELCOME");
            welcomeNotification.setTitle("Welcome to Mello! 🌿");
            welcomeNotification.setMessage("We're so glad you've joined our community. Start your journey today!");
            welcomeNotification.setStatus("DELIVERED");
            welcomeNotification.setCreatedAt(LocalDateTime.now());
            welcomeNotification.setDeliveredAt(LocalDateTime.now());
            welcomeNotification.setRead(false);
            notificationRepository.save(welcomeNotification);
            
        } catch (Exception e) {
            System.err.println("Failed to send welcome email or notification: " + e.getMessage());
        }
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
             User user = userOpt.get();
             user.setLastActiveAt(System.currentTimeMillis());
             userRepository.save(user);
             return ResponseEntity.ok(user);
         }
        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(System.currentTimeMillis() + 3600000); // 1 hour
            userRepository.save(user);
            emailService.sendPasswordResetEmail(email, token);
        }
        // Always return OK to avoid email enumeration
        return ResponseEntity.ok("If an account exists with that email, a reset link has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetToken()) && u.getResetTokenExpiry() > System.currentTimeMillis())
                .findFirst();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            return ResponseEntity.ok("Password reset successful");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }
}
