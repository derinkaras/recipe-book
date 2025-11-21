package com.derinkaras.recipebook.controller;
import com.derinkaras.recipebook.dto.recipe.RecipeDto;
import com.derinkaras.recipebook.dto.user.CreateUserRequest;
import com.derinkaras.recipebook.dto.user.UpdateUserRequest;
import com.derinkaras.recipebook.dto.user.UserDto;
import com.derinkaras.recipebook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Use Path Variables when referring to a unique resource from its direct table
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto registerUser(@Valid @RequestBody CreateUserRequest user) {
        return userService.registerUser(user);
    }

    // Use Path Variables when referring to a unique resource
    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest req
    ) {
        return userService.updateUser(id, req);
    }

    // Request params for filtering and nondirect access
    @GetMapping
    public List<RecipeDto> getUserRecipes(
            @RequestParam Long userId
    ) {
        return userService.getUserRecipes(userId);
    }


}
