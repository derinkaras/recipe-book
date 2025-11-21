package com.derinkaras.recipebook.dto.ingredient;

import jakarta.validation.constraints.NotBlank;

public class CreateIngredientRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
