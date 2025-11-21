package com.derinkaras.recipebook.controller;

import com.derinkaras.recipebook.dto.ingredient.CreateIngredientRequest;
import com.derinkaras.recipebook.dto.ingredient.IngredientDto;
import com.derinkaras.recipebook.service.IngredientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("api/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getAll(){
        return ingredientService.getAll();
    }

    @GetMapping("/{id}")
    public IngredientDto getById(@PathVariable Long id){
        return ingredientService.getById(id);
    }

    @PostMapping
    public IngredientDto create(@Valid @RequestBody CreateIngredientRequest req){
        return ingredientService.create(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ingredientService.delete(id);
        // res code 203 saying that the request was successful and that there is no body in the response
        return ResponseEntity.noContent().build();
    }





}
