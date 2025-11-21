package com.derinkaras.recipebook.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String difficulty;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;


    // This entity is the *owning side* of the many-to-many relationship.
    //
    // A many-to-many relationship requires a join table. The two annotations work as follows:
    //
    //   - joinColumns = @JoinColumn(name = "recipe_id")
    //       → The foreign key column in the join table that points to THIS entity (Recipe).
    //
    //   - inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    //       → The foreign key column in the join table that points to the OTHER entity (Ingredient).
    //
    // In short:
    //   joinColumns        = FK to the entity where the annotation is written.
    //   inverseJoinColumns = FK to the related entity on the other side of the relationship.

    @ManyToMany
    @JoinTable(
            name="recipe_ingredient",
            joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name="ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
