package com.recipevault.recipevault.recipe;

import com.recipevault.recipevault.user.User;
import com.recipevault.recipevault.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeController {


    private final RecipeService recipeService;
    private final UserRepository userRepository;

    public RecipeController(RecipeService recipeService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    /**
     * Helper method to extract the authenticated user from the JWT token.
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    /**
     * Create a new recipe for the authenticated user.
     */
    @PostMapping("/recipe")
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.createRecipe(recipe, user);
    }

    /**
     * Retrieve a recipe by ID.
     */
    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(id);
        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(recipe);
    }

    /**
     * Retrieve all recipes for the authenticated user.
     */
    @GetMapping("/user/recipe")
    public List<Recipe> getRecipesByUserId(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.getRecipesByUserId(user.getId());
    }

    /**
     * Update an existing recipe for the authenticated user.
     */
    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(id);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDetails, user);
        return ResponseEntity.ok(updatedRecipe);
    }

    /**
     * Delete a recipe for the authenticated user.
     */
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(id);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeService.deleteRecipe(id, user);
        return ResponseEntity.ok("Recipe deleted successfully.");
    }

    /**
     * Search for a recipe by title for the authenticated user.
     */
    @GetMapping("/recipe/search")
    public ResponseEntity<Recipe> searchRecipeByTitle(@RequestParam String title, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Optional<Recipe> recipe = recipeService.findByTitleForUser(title, user);
        if (recipe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(recipe.get());
    }
}