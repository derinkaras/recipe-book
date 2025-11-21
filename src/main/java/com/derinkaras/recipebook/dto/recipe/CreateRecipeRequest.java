package com.derinkaras.recipebook.dto.recipe;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateRecipeRequest {
    // This DTO is used to receive JSON data when a client creates a new recipe.
    //
    // Note about setters:
    // Since this is a regular Java class (NOT a Java record), Spring uses Jackson to
    // deserialize incoming JSON into this object. Jackson first creates an empty
    // CreateRecipeRequest instance using the no-args constructor, and then populates
    // each field by calling the corresponding setter:
    //
    //   title → setTitle(...)
    //   description → setDescription(...)
    //   difficulty → setDifficulty(...)
    //   ownerId → setOwnerId(...)
    //   ingredientIds → setIngredientIds(...)
    //
    // Without these setters, Jackson would be unable to map the incoming JSON,
    // and all fields would remain null.
    //
    // If this DTO were a Java record, setters would not be required because
    // records use constructor-based binding instead.


    @NotNull(message = "title is required")
    private String title;

    private String description;

    @NotNull(message = "difficulty is required")
    private String difficulty;

    @NotNull(message = "owner id is required")
    private Long ownerId;

    private List<Long> ingredientIds;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}
