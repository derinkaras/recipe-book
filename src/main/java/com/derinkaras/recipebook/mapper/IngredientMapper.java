package com.derinkaras.recipebook.mapper;

import com.derinkaras.recipebook.dto.ingredient.IngredientDto;
import com.derinkaras.recipebook.model.Ingredient;

public class IngredientMapper {
    public static IngredientDto toDto (Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        return ingredientDto;
    }
}
