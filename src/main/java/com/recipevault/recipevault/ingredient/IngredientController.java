package com.recipevault.recipevault.ingredient;

import com.recipevault.recipevault.recipe.Recipe;
import com.recipevault.recipevault.recipe.RecipeService;
import com.recipevault.recipevault.user.User;
import com.recipevault.recipevault.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UserRepository userRepository) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve the authenticated user based on the provided authentication token.
     *
     * @param authentication the authentication token
     * @return the authenticated user
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    /**
     * Create a new ingredient and add it to a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param ingredient     the ingredient to be added
     * @param authentication the authentication token
     * @return the created ingredient
     */
    @PostMapping("/recipes/{recipeId}/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@PathVariable Long recipeId, @RequestBody Ingredient ingredient, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Ingredient createdIngredient = ingredientService.createIngredient(recipe, ingredient);
        return ResponseEntity.ok(createdIngredient);
    }

    /**
     * Find all ingredients for a specific recipe.
     *
     * @param recipeId       the ID of the recipe
     * @param authentication the authentication token
     * @return a list of ingredients for the specified recipe
     */
    @GetMapping("/recipes/{recipeId}/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredientsByRecipe(@PathVariable Long recipeId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<Ingredient> ingredients = ingredientService.getAllIngredientsByRecipeId(recipeId);
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Find an ingredient by its ID and recipe.
     *
     * @param recipeId       the ID of the recipe
     * @param ingredientId   the ID of the ingredient
     * @param authentication the authentication token
     * @return the ingredient if found, otherwise a 404 response
     */
    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long recipeId, @PathVariable Long ingredientId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Optional<Ingredient> ingredient = ingredientService.getIngredientByIdAndRecipeId(ingredientId, recipeId);
        return ingredient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all ingredients across all recipes belonging to the authenticated user.
     *
     * @param authentication The authentication object containing user information.
     * @return A list of all ingredients across all recipes for the authenticated user.
     */
    @GetMapping("/users/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredientsByUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Ingredient> ingredients = ingredientService.getAllIngredientsByUserId(user.getId());
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Update an ingredient in a specific recipe for the authenticated user.
     *
     * @param recipeId          the ID of the recipe
     * @param ingredientId      the ID of the ingredient
     * @param ingredientDetails the updated ingredient details
     * @param authentication    the authentication token
     * @return the updated ingredient
     */
    @PutMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, @RequestBody Ingredient ingredientDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, recipeId, ingredientDetails);
        return ResponseEntity.ok(updatedIngredient);
    }

    /**
     * Delete an ingredient from a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param ingredientId   the ID of the ingredient
     * @param authentication the authentication token
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        ingredientService.deleteIngredient(ingredientId, recipeId);
        return ResponseEntity.ok("Ingredient deleted successfully.");
    }
}
