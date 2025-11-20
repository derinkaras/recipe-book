package com.derinkaras.recipebook.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users") // avoid the reserved key word "user"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private LocalDateTime createdDate = LocalDateTime.now();

    // This entity participates in a one-to-one relationship with UserProfile.
    // In a one-to-one, only ONE table contains the foreign key (the "owner" side).
    //
    // Because this annotation uses `mappedBy = "user"`, it means:
    //   → The UserProfile entity owns the relationship,
    //   → And it has the foreign key column (via @JoinColumn on its `user` field).
    //
    // This class (User) is the *inverse side* of the relationship. It does not create
    // a foreign key column — it simply references the relationship defined in UserProfile.
    //
    // cascade = CascadeType.ALL
    //     Any persistence operation performed on User (save, update, delete)
    //     will also be applied to its UserProfile.
    //
    // orphanRemoval = true
    //     If this User is detached from its profile (profile set to null),
    //     the UserProfile row becomes an orphan and is automatically deleted.
    //
    // Summary:
    //   - UserProfile = owner (has FK)
    //   - User        = inverse side (`mappedBy = "user"`)
    //   - Cascade ensures child follows parent’s lifecycle
    //   - Orphan removal deletes the profile if it’s no longer attached

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @OneToMany(mappedBy = "owner")
    private List<Recipe> recipes = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
