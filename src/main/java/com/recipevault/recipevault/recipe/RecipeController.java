package com.recipevault.recipevault.recipe;

import com.recipevault.recipevault.user.User;
import com.recipevault.recipevault.user.UserRepository;
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
     * Create a new recipe for the authenticated user.
     */
    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.createRecipe(recipe, user);
    }

    /**
     * Retrieve a recipe by ID.
     */
    @GetMapping("/recipes/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    /**
     * Retrieve all recipes for the authenticated user.
     */
    @GetMapping("/users/recipes")
    public List<Recipe> getRecipesByUserId(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.getRecipesByUserId(user.getId());
    }

    /**
     * Update an existing recipe for the authenticated user.
     */
    @PutMapping("/recipes/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.updateRecipe(id, recipeDetails, user);
    }

    /**
     * Delete a recipe for the authenticated user.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        recipeService.deleteRecipe(id, user);
        return ResponseEntity.ok("Recipe deleted successfully.");
    }

    /**
     * Search for a recipe by title for the authenticated user.
     */
    @GetMapping("/recipes/search")
    public Recipe searchRecipeByTitle(@RequestParam String title, Authentication authentication) {
        // Get the authenticated user
        User user = getAuthenticatedUser(authentication);

        // Call the service to search for the recipe by title for the specific user
        Optional<Recipe> recipe = recipeService.findByTitleForUser(title, user);

        // Handle the case when the recipe is not found
        return recipe.orElseThrow(() -> new RuntimeException("Recipe not found for title: " + title));
    }

    /**
     * Helper method to extract the authenticated user from the JWT token.
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}