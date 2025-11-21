package com.derinkaras.recipebook.model;

import jakarta.persistence.*;

@Entity

public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(length = 500)
    private String bio;


    // This entity is the *owning side* of the one-to-one relationship with User.
    // The owning side is the one that contains the foreign key column in the database.
    //
    // @JoinColumn(name = "user_id")
    //     This creates the actual foreign key column (user_id) in the user_profile table.
    //     The column will store the primary key of the associated User.
    //
    // In Java, we reference the entire User object (private User user).
    // Hibernate automatically extracts the User's primary key and stores it
    // in the user_id column when persisting this entity.
    //
    // Because this class owns the relationship, the User entity must reference it
    // using `mappedBy = "user"` on its side of the @OneToOne annotation.
    //
    // Summary:
    //   - UserProfile = owner (defines @JoinColumn and holds the FK)
    //   - User        = inverse side (uses mappedBy)
    //   - The FK column user_id is generated in the UserProfile table
    //   - Hibernate stores User’s PK in user_id when saving a UserProfile

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
