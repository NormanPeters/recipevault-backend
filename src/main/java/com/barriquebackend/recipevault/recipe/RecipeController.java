package com.barriquebackend.recipevault.recipe;

import com.barriquebackend.user.User;
import com.barriquebackend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling recipe-related endpoints.
 * Provides endpoints for creating, retrieving, updating, and deleting recipes.
 */
@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;

    /**
     * Constructs a RecipeController with the specified RecipeService and UserRepository.
     *
     * @param recipeService  the service for recipe business logic
     * @param userRepository the repository for user data
     */
    public RecipeController(RecipeService recipeService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all recipes for the authenticated user.
     *
     * @param authentication the authentication token containing user details
     * @return a list of recipes belonging to the authenticated user
     */
    @GetMapping("/user/recipe")
    public List<Recipe> getRecipesByUserId(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.getRecipesByUserId(user.getId());
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id             the ID of the recipe
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the recipe if found and authorized, or an appropriate error status
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
     * Creates a new recipe for the authenticated user.
     *
     * @param recipe         the recipe data to create
     * @param authentication the authentication token containing user details
     * @return the created recipe
     */
    @PostMapping("/recipe")
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return recipeService.createRecipe(recipe, user);
    }

    /**
     * Updates an existing recipe for the authenticated user.
     *
     * @param id             the ID of the recipe to update
     * @param recipeDetails  the updated recipe data
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the updated recipe if successful, or an error status
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
     * Deletes a recipe for the authenticated user.
     *
     * @param id             the ID of the recipe to delete
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with a success message if deletion is successful, or an error status
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
     * Helper method to extract the authenticated user from the security context.
     *
     * @param authentication the authentication token containing user details
     * @return the authenticated User
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}