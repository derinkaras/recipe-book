package com.derinkaras.recipebook.mapper;

import com.derinkaras.recipebook.model.Recipe;
import com.derinkaras.recipebook.dto.recipe.RecipeDto;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static RecipeDto toDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setDifficulty(recipe.getDifficulty());
        recipeDto.setCreatedAt(recipe.getCreatedAt());

        // Because they can live independently of each other
        if (recipe.getOwner() != null) {
            recipeDto.setOwnerId(recipe.getOwner().getId());
        }

        // This is the main filter, instead of providing ingredient objs we make it so the dto provide just their
        // names
        List<String> ingredientNames = recipe.getIngredients().stream()
                .map(ingredient -> ingredient.getName())
                .collect(Collectors.toList());

        recipeDto.setIngredientNames(ingredientNames);

        return recipeDto;

    }


}
