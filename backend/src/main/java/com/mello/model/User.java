package com.mello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String fullName;
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @JsonIgnore
    private String resetToken;
    
    @JsonIgnore
    private Long resetTokenExpiry;
    
    private String profilePicture;
     private Long lastActiveAt;
 
     public User() {}
 
     public User(String fullName, String email, String password) {
         this.fullName = fullName;
         this.email = email;
         this.password = password;
     }
 
     public String getId() { return id; }
     public void setId(String id) { this.id = id; }
     public String getFullName() { return fullName; }
     public void setFullName(String fullName) { this.fullName = fullName; }
     public String getEmail() { return email; }
     public void setEmail(String email) { this.email = email; }
     public String getPassword() { return password; }
     public void setPassword(String password) { this.password = password; }
     public String getResetToken() { return resetToken; }
     public void setResetToken(String resetToken) { this.resetToken = resetToken; }
     public Long getResetTokenExpiry() { return resetTokenExpiry; }
     public void setResetTokenExpiry(Long resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }
     public String getProfilePicture() { return profilePicture; }
     public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
     public Long getLastActiveAt() { return lastActiveAt; }
     public void setLastActiveAt(Long lastActiveAt) { this.lastActiveAt = lastActiveAt; }
 }
