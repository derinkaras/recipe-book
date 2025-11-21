package com.derinkaras.recipebook.service;
import com.derinkaras.recipebook.dto.recipe.CreateRecipeRequest;
import com.derinkaras.recipebook.dto.recipe.RecipeDto;
import com.derinkaras.recipebook.dto.recipe.UpdateRecipeRequest;
import com.derinkaras.recipebook.exception.ResourceNotFoundException;
import com.derinkaras.recipebook.mapper.RecipeMapper;
import com.derinkaras.recipebook.model.Ingredient;
import com.derinkaras.recipebook.model.Recipe;
import com.derinkaras.recipebook.model.User;
import com.derinkaras.recipebook.respository.IngredientRepository;
import com.derinkaras.recipebook.respository.RecipeRepository;
import com.derinkaras.recipebook.respository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // Find by something which is not unique may return many results
    public List<RecipeDto> getAll(Long ownerId) {
        List<Recipe> recipes = (ownerId != null) ?
                recipeRepository.findByOwnerId(ownerId) :
                recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDto(recipe))
                .toList();
    }

    public RecipeDto getById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Recipe", recipeId
                ));
        return RecipeMapper.toDto(recipe);
    }

    public RecipeDto create(CreateRecipeRequest req) {
        User owner = userRepository.findById(req.getOwnerId())
                .orElseThrow(()-> new ResourceNotFoundException("User", req.getOwnerId()));

        Recipe recipe = new Recipe();
        recipe.setTitle(req.getTitle());
        recipe.setDescription(req.getDescription());
        recipe.setDifficulty(req.getDifficulty());
        recipe.setOwner(owner);

        // findAllById is specifically designed to take an arg of ids
        // it then returns the repo type in a list which matches those ids
        // The HashSet takes a lit as args and converts it to a set
        if (req.getIngredientIds() != null && !req.getIngredientIds().isEmpty()) {
            Set<Ingredient> ingredients = new HashSet<>(ingredientRepository.findAllById(req.getIngredientIds()));
            recipe.setIngredients(ingredients);
        }
        Recipe saved = recipeRepository.save(recipe);
        return RecipeMapper.toDto(saved);
    }

    public RecipeDto update(Long recipeId, UpdateRecipeRequest req) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()-> new ResourceNotFoundException("Recipe", recipeId));

        if (req.getTitle() != null) recipe.setTitle(req.getTitle());
        if (req.getDescription() != null) recipe.setDescription(req.getDescription());
        if (req.getDifficulty() != null) recipe.setDifficulty(req.getDifficulty());

        if (req.getIngredientIds() != null && !req.getIngredientIds().isEmpty()) {
            var ingredients = new HashSet<>(ingredientRepository.findAllById(req.getIngredientIds()));
            recipe.setIngredients(ingredients);
        }
        Recipe updated = recipeRepository.save(recipe);
        return RecipeMapper.toDto(updated);
    }

    public void deleteById(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe", recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }


}
