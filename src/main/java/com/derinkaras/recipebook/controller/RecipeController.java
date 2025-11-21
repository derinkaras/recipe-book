package com.derinkaras.recipebook.controller;


import com.derinkaras.recipebook.dto.CreateRecipeRequest;
import com.derinkaras.recipebook.dto.RecipeDto;
import com.derinkaras.recipebook.dto.UpdateRecipeRequest;
import com.derinkaras.recipebook.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAll(@RequestParam(required = false) Long ownerId) {
        return recipeService.getAll(ownerId);
    }

    @GetMapping("/{id}")
    public RecipeDto getOne(@PathVariable Long id){
        return recipeService.getById(id);
    }

    // @Valid triggers validation on the incoming JSON before this method runs.
    // If the JSON does NOT satisfy the CreateRecipeRequest DTO (e.g. missing @NotNull fields),
    // Spring throws MethodArgumentNotValidException BEFORE entering the method.
    //
    // Because we have @RestControllerAdvice with an @ExceptionHandler for that exception,
    // Spring immediately sends the error ResponseEntity defined in GlobalExceptionHandler,
    // and SKIPS the normal "return ResponseEntity.ok(...)" below.
    //
    // In short: invalid request → controller does NOT run → GlobalExceptionHandler returns 400.
    @PostMapping
    public ResponseEntity<RecipeDto> create(@Valid @RequestBody CreateRecipeRequest recipe) {
        RecipeDto created = recipeService.create(recipe);
        return ResponseEntity.ok(created);
    }

    // This UpdateRecipeRequest DTO doesnt have any not null fields so it doesnt need to be validated
    // Within the service method to update it performs checks and updates if needed still only returning the dto
    @PutMapping("/{id}")
    public RecipeDto update(
            @PathVariable Long id,
            @RequestBody UpdateRecipeRequest req
            ) {
        return recipeService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        recipeService.deleteById(id);
        // res code 204 saying that the request was successful no body in the response
        return ResponseEntity.noContent().build();
    }

}
