package com.mello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mellohealthsupport@gmail.com");
        message.setTo(to);
        message.setSubject("Welcome to Mello! 🌿");
        message.setText("Hello " + name + ",\n\n" +
                "Welcome to Mello! We're so glad you've joined our community. " +
                "Mello is here to help you track your mind, manage your habits, and find your inner calm.\n\n" +
                "Start your journey today by logging your first reflection!\n\n" +
                "Stay mindful,\nThe Mello Team");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mellohealthsupport@gmail.com");
        message.setTo(to);
        message.setSubject("Reset Your Mello Password");
        message.setText("You requested a password reset. Please use the following link to reset your password:\n\n" +
                "http://localhost:5173/reset-password/" + token + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you didn't request this, please ignore this email.");
        mailSender.send(message);
    }

    // NOURHAN
    public void sendNotificationEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mellohealthsupport@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHabitReminderEmail(String to, String userName, String habitName, String timeMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mellohealthsupport@gmail.com");
        message.setTo(to);
        message.setSubject("Mello Reminder: Time for " + habitName + "!");
        message.setText("Hello " + userName + ",\n\n" +
                timeMessage + " to complete your habit: " + habitName + ".\n\n" +
                "Take a deep breath and jump right in!\n\n" +
                "Stay mindful,\nThe Mello Team");
        mailSender.send(message);
    }
}
