package com.derinkaras.recipebook.service;

import com.derinkaras.recipebook.dto.recipe.RecipeDto;
import com.derinkaras.recipebook.dto.user.*;
import com.derinkaras.recipebook.exception.DuplicateResourceException;
import com.derinkaras.recipebook.exception.ResourceNotFoundException;
import com.derinkaras.recipebook.mapper.RecipeMapper;
import com.derinkaras.recipebook.mapper.UserMapper;
import com.derinkaras.recipebook.mapper.UserProfileMapper;
import com.derinkaras.recipebook.model.User;
import com.derinkaras.recipebook.model.UserProfile;
import com.derinkaras.recipebook.respository.RecipeRepository;
import com.derinkaras.recipebook.respository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
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

    // NOTE on one-to-many (User → Recipes):
    // You only set the relationship on the owning side (recipe.setOwner(user)).
    // Hibernate uses the FK (recipe.owner_id) to rebuild user.getRecipes() by querying the DB.
    //
    // The ONLY time "not adding the recipe to user.getRecipes()" is a problem:
    // → In the SAME in-memory User object where you just created and saved a Recipe.
    //   Saving the Recipe updates the DB, but DOES NOT update the existing User.recipes list.
    //   You must reload the User or manually add the recipe to keep the in-memory graph in sync.
    //
    // Example:
    //
    // User user = userRepository.findById(1L).get();   // loads recipes = [A, B]
    //
    // Recipe r = new Recipe();
    // r.setOwner(user);
    // recipeRepository.save(r);                        // DB now has [A, B, C]
    //
    // user.getRecipes();                               // still [A, B] in memory (NOT auto-updated)
    //
    // Solution options:
    // 1) Reload the user: user = userRepository.findById(1L).get();  // now gets [A, B, C]
    // 2) Or manually sync: user.getRecipes().add(r);
    //
    // In all normal API cases (new request → fresh fetch), user.getRecipes() is always correct.



    public List<RecipeDto> getUserRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return user.getRecipes().stream()
                .map(recipe -> RecipeMapper.toDto(recipe))
                .toList();
    }

    // A User and a UserProfile are two different resources. Therefor they will have different controllers but
    // will have the same service
    public UserProfileDto createUserProfile(Long userId, CreateUserProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        UserProfile profile = new UserProfile();
        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());
        if (req.getBio() != null) {
            profile.setBio(req.getBio());
        }
        profile.setUser(user);

        // check out notes for why this is not needed but will include anyways
        user.setProfile(profile);

        // We save the USER (not the profile) because User is the aggregate root.
        // With cascade = ALL on user.userProfile, saving the user automatically
        // saves the linked UserProfile. Cascade does NOT auto-create a profile;
        // it only persists the profile IF we attach it to the user. Therefor we
        // must: (1) create profile, (2) link both sides, (3) save the user.
        User saved = userRepository.save(user);
        return UserProfileMapper.userProfileToDto(saved.getProfile());
    }

    public UserProfileDto updateUserProfile(Long userId, UpdateUserProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (req.getFirstName() != null) {
            user.getProfile().setFirstName(req.getFirstName());
        }
        if (req.getLastName() != null) {
            user.getProfile().setLastName(req.getLastName());
        }
        if (req.getBio() != null) {
            user.getProfile().setBio(req.getBio());
        }
        User saved = userRepository.save(user);
        return UserProfileMapper.userProfileToDto(saved.getProfile());
    }





}
