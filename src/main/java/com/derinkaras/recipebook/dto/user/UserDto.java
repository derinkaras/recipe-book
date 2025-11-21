package com.derinkaras.recipebook.dto.user;
import com.derinkaras.recipebook.model.Recipe;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UserDto {
    private Long id;
    private String email;
    private String username;
    private UserProfileDto profile;

    public UserProfileDto getProfile() {
        return profile;
    }

    public void setProfile(UserProfileDto profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
