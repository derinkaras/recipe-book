package com.derinkaras.recipebook.dto;

import java.util.List;

// DTO used for updating a recipe.
// All fields are optional — the user may send any subset of them.
public class UpdateRecipeRequest {

    private String title;
    private String description;
    private String difficulty;
    private List<Long> ingredientIds;

    // getters + setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<Long> getIngredientIds() { return ingredientIds; }
    public void setIngredientIds(List<Long> ingredientIds) { this.ingredientIds = ingredientIds; }

}
