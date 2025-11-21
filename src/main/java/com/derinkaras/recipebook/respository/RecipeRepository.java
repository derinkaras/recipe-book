package com.derinkaras.recipebook.respository;

import com.derinkaras.recipebook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Spring Data JPA automatically implements this method using its
    // "derived query" mechanism:
    //
    //   findBy      → tells Spring to generate a SELECT query
    //   Owner       → matches the 'owner' field inside the Recipe entity
    //   Id          → matches the 'id' field inside the Owner (User) entity
    //
    // Combined, Spring interprets this as:
    //   SELECT * FROM recipes WHERE owner_id = :ownerId
    //
    // No manual SQL or implementation needed — Spring generates it from the name.
    List<Recipe> findByOwnerId(Long ownerId);
}

