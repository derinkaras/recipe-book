package com.derinkaras.recipebook.respository;

import com.derinkaras.recipebook.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}
