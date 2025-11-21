package com.derinkaras.recipebook.mapper;

import com.derinkaras.recipebook.dto.user.UserProfileDto;
import com.derinkaras.recipebook.model.UserProfile;

public class UserProfileMapper {
    public static UserProfileDto userProfileToDto(UserProfile userProfile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFirstName(userProfile.getFirstName());
        userProfileDto.setLastName(userProfile.getLastName());
        if (userProfile.getBio() != null) {
            userProfileDto.setBio(userProfile.getBio());
        }
        return userProfileDto;
    }
}
