package com.derinkaras.recipebook.controller;

import com.derinkaras.recipebook.dto.user.CreateUserProfileRequest;
import com.derinkaras.recipebook.dto.user.UpdateUserProfileRequest;
import com.derinkaras.recipebook.dto.user.UserProfileDto;
import com.derinkaras.recipebook.service.UserService;
import org.springframework.web.bind.annotation.*;


// the userId here is a global path variable
@RestController
@RequestMapping("api/v1/user/{userId}/profile")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserProfileDto createProfile(
            @PathVariable Long userId,
            @RequestBody CreateUserProfileRequest req
    ) {
        return userService.createUserProfile(userId, req);
    }

    @PutMapping
    public UserProfileDto updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserProfileRequest req
    ) {
        return userService.updateUserProfile(userId, req);
    }

}
