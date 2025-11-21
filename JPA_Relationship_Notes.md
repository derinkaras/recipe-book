
# 📘 JPA Relationship Notes (User, UserProfile, Recipe, Ingredient)

---

# 1️⃣ One-to-One — User ↔ UserProfile

### Entities
```java
// User (inverse side)
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private UserProfile profile;

// UserProfile (owning side)
@OneToOne
@JoinColumn(name = "user_id", nullable = false, unique = true)
private User user;
```

### Key Points
- Owning side: **UserProfile** (contains FK `user_id`)
- Inverse side: **User**
- Hibernate queries: **user_profile** table when calling `user.getProfile()`

### Saving Example
```java
User user = userRepo.findById(id).get();

UserProfile profile = new UserProfile();
profile.setUser(user); // only owning side required
userProfileRepo.save(profile);
```

### Lazy Loading
```java
User u = userRepo.findById(id).get();
u.getProfile(); 
// SELECT * FROM user_profile WHERE user_id = ?
```

---

# 2️⃣ One-to-Many — User ↔ Recipe

### Entities
```java
// User (inverse side)
@OneToMany(mappedBy = "owner")
private List<Recipe> recipes;

// Recipe (owning side)
@ManyToOne
@JoinColumn(name = "owner_id")
private User owner;
```

### Key Points
- Owning side: **Recipe** (FK `owner_id`)
- Hibernate queries: **recipe** table for `user.getRecipes()`

### Saving Example
```java
User user = userRepo.findById(id).get();

Recipe r = new Recipe();
r.setOwner(user);
recipeRepo.save(r);
```

### Lazy Loading
```java
User u = userRepo.findById(id).get();
u.getRecipes(); 
// SELECT * FROM recipe WHERE owner_id = ?
```

### Important Note
Inside the same in-memory User instance:
```java
user.getRecipes(); // may not include newly created recipe until user is reloaded
```

---

# 3️⃣ Many-to-Many — Recipe ↔ Ingredient

### Entities
```java
// Recipe (owning side)
@ManyToMany
@JoinTable(
        name="recipe_ingredient",
        joinColumns = @JoinColumn(name="recipe_id"),
        inverseJoinColumns = @JoinColumn(name="ingredient_id")
)
private Set<Ingredient> ingredients = new HashSet<>();

// Ingredient (inverse side)
@ManyToMany(mappedBy = "ingredients")
private Set<Recipe> recipes = new HashSet<>();
```

### Key Points
- Owning side: **Recipe.ingredients**
- Hibernate stores mapping in: **recipe_ingredient (recipe_id, ingredient_id)**
- Inverse side (`Ingredient.recipes`) is automatically populated via lazy load

### Saving Example
```java
Recipe recipe = new Recipe();
recipe.setOwner(owner);

Set<Ingredient> ingredients =
        new HashSet<>(ingredientRepository.findAllById(req.getIngredientIds()));

recipe.setIngredients(ingredients);

recipeRepository.save(recipe);
// Hibernate inserts into recipe_ingredient table
```

### Lazy Loading
```java
Recipe r = recipeRepo.findById(id).get();
r.getIngredients(); 
// SELECT ingredients via join table

Ingredient ing = ingredientRepo.findById(id).get();
ing.getRecipes(); 
// SELECT recipes via join table
```

### Important Note
You do **NOT** need to manually do:
```java
ingredient.getRecipes().add(recipe);
```

Hibernate reconstructs the inverse side entirely from the join table.

---
