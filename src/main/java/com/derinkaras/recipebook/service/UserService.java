package com.derinkaras.recipebook.service;

import com.derinkaras.recipebook.dto.recipe.RecipeDto;
import com.derinkaras.recipebook.dto.user.CreateUserRequest;
import com.derinkaras.recipebook.dto.user.UpdateUserRequest;
import com.derinkaras.recipebook.dto.user.UserDto;
import com.derinkaras.recipebook.exception.DuplicateResourceException;
import com.derinkaras.recipebook.exception.ResourceNotFoundException;
import com.derinkaras.recipebook.mapper.RecipeMapper;
import com.derinkaras.recipebook.mapper.UserMapper;
import com.derinkaras.recipebook.model.User;
import com.derinkaras.recipebook.respository.RecipeRepository;
import com.derinkaras.recipebook.respository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RecipeRepository recipeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserDto registerUser(CreateUserRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());

        // Hash the raw password before setting
        String hashed = passwordEncoder.encode(user.getPassword());
        userEntity.setPassword(hashed);
        User saved =  userRepository.save(userEntity);
        return UserMapper.toUserDto(saved);
    }

    // We can use the create user request dto since it has all fields
    public UserDto updateUser(Long userId, UpdateUserRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (req.getUsername() != null) {
            if (userRepository.existsByUsername(req.getUsername())) {
                throw new DuplicateResourceException("Username already exists");
            } else {
                user.setUsername(req.getUsername());
            }
        }
        if (req.getEmail() != null) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            } else {
                user.setEmail(req.getEmail());
            }
        }
        if (req.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        User saved = userRepository.save(user);
        return UserMapper.toUserDto(saved);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return UserMapper.toUserDto(user);
    }



    // Remember: JPA automatically populates user.getRecipes() because User ↔ Recipe is a
    // bidirectional One-To-Many / Many-To-One relationship.
    //
    // HOW IT WORKS:
    //
    // 1. The "owning side" of the relationship is in Recipe:
    //
    //      @ManyToOne
    //      @JoinColumn(name = "owner_id")
    //      private User owner;
    //
    //    This side contains the actual foreign key (recipe.owner_id) stored in the database.
    //    Whenever a Recipe is saved with recipe.setOwner(user), JPA writes owner_id = user.id
    //    into the recipe table.
    //
    // 2. User has the inverse side:
    //
    //      @OneToMany(mappedBy = "owner")
    //      private List<Recipe> recipes;
    //
    //    Because this is the *inverse* side, it does NOT control the relationship and does NOT
    //    need to be manually updated. When you load a User entity, Hibernate knows how to find
    //    all Recipe rows whose owner_id matches the user's ID.
    //
    // 3. So when you call user.getRecipes(), JPA executes a query like:
    //
    //      SELECT * FROM recipe WHERE owner_id = :userId
    //
    //    and fills the List<Recipe> automatically. No manual .add() calls are required.
    //
    // 4. This method simply retrieves that auto-populated list and maps each Recipe entity
    //    into its corresponding DTO.
    //
    // This is why getUserRecipes() can rely on user.getRecipes() without additional logic.
    public List<RecipeDto> getUserRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return user.getRecipes().stream()
                .map(recipe -> RecipeMapper.toDto(recipe))
                .toList();
    }


}
