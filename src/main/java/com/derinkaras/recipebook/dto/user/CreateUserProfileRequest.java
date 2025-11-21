package com.derinkaras.recipebook.dto.user;

import jakarta.validation.constraints.NotNull;

public class CreateUserProfileRequest {
    @NotNull(message = "Need a first name")
    String firstName;
    @NotNull(message = "Need a last name")
    String lastName;
    String bio;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
