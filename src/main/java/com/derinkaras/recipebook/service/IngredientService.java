package com.derinkaras.recipebook.service;

import com.derinkaras.recipebook.dto.ingredient.CreateIngredientRequest;
import com.derinkaras.recipebook.dto.ingredient.IngredientDto;
import com.derinkaras.recipebook.exception.DuplicateResourceException;
import com.derinkaras.recipebook.exception.ResourceNotFoundException;
import com.derinkaras.recipebook.mapper.IngredientMapper;
import com.derinkaras.recipebook.model.Ingredient;
import com.derinkaras.recipebook.respository.IngredientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.valves.rewrite.ResolverImpl;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientDto create (CreateIngredientRequest req) {
        if (ingredientRepository.findByName(req.getName()) != null) {
            throw new DuplicateResourceException("Ingredient already exists");
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(req.getName());
        Ingredient saved = ingredientRepository.save(ingredient);
        return IngredientMapper.toDto(saved);
    }

    public void delete (Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingredient", id);
        }
        ingredientRepository.deleteById(id);
    }

    public List<IngredientDto> getAll() {
        List<IngredientDto> ingredients = ingredientRepository.findAll()
                .stream().map(ingredient -> IngredientMapper.toDto(ingredient)).toList();
        return ingredients;
    }

    public IngredientDto getById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Ingredient", id)
                );
        return IngredientMapper.toDto(ingredient);
    }


}
