package com.recipevault.recipevault.nutritionalValue;

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
public class NutritionalValueController {

    private final NutritionalValueService nutritionalValueService;
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    public NutritionalValueController(NutritionalValueService nutritionalValueService, RecipeService recipeService, UserRepository userRepository) {
        this.nutritionalValueService = nutritionalValueService;
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
     * Create a new nutritional value and add it to a recipe for the authenticated user.
     *
     * @param recipeId           the ID of the recipe
     * @param nutritionalValue   the nutritional value to be added
     * @param authentication     the authentication token
     * @return the created nutritional value
     */
    @PostMapping("/recipes/{recipeId}/nutritionalValues")
    public ResponseEntity<NutritionalValue> createNutritionalValue(@PathVariable Long recipeId, @RequestBody NutritionalValue nutritionalValue, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        NutritionalValue createdNutritionalValue = nutritionalValueService.createNutritionalValue(recipe, nutritionalValue);
        return ResponseEntity.ok(createdNutritionalValue);
    }

    /**
     * Find all nutritional values for a specific recipe.
     *
     * @param recipeId           the ID of the recipe
     * @param authentication     the authentication token
     * @return a list of nutritional values for the specified recipe
     */
    @GetMapping("/recipes/{recipeId}/nutritionalValues")
    public ResponseEntity<List<NutritionalValue>> getAllNutritionalValuesByRecipe(@PathVariable Long recipeId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<NutritionalValue> nutritionalValues = nutritionalValueService.getAllNutritionalValuesByRecipeId(recipeId);
        return ResponseEntity.ok(nutritionalValues);
    }

    /**
     * Find a nutritional value by its ID and recipe.
     *
     * @param recipeId           the ID of the recipe
     * @param nutritionalValueId the ID of the nutritional value
     * @param authentication     the authentication token
     * @return the nutritional value if found, otherwise a 404 response
     */
    @GetMapping("/recipes/{recipeId}/nutritionalValues/{nutritionalValueId}")
    public ResponseEntity<NutritionalValue> getNutritionalValueById(@PathVariable Long recipeId, @PathVariable Long nutritionalValueId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Optional<NutritionalValue> nutritionalValue = nutritionalValueService.getNutritionalValueByIdAndRecipeId(nutritionalValueId, recipeId);
        return nutritionalValue.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a nutritional value in a specific recipe for the authenticated user.
     *
     * @param recipeId           the ID of the recipe
     * @param nutritionalValueId the ID of the nutritional value
     * @param nutritionalDetails the updated nutritional value details
     * @param authentication     the authentication token
     * @return the updated nutritional value
     */
    @PutMapping("/recipes/{recipeId}/nutritionalValues/{nutritionalValueId}")
    public ResponseEntity<NutritionalValue> updateNutritionalValue(@PathVariable Long recipeId, @PathVariable Long nutritionalValueId, @RequestBody NutritionalValue nutritionalDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        NutritionalValue updatedNutritionalValue = nutritionalValueService.updateNutritionalValue(nutritionalValueId, recipeId, nutritionalDetails);
        return ResponseEntity.ok(updatedNutritionalValue);
    }

    /**
     * Delete a nutritional value from a recipe for the authenticated user.
     *
     * @param recipeId           the ID of the recipe
     * @param nutritionalValueId the ID of the nutritional value
     * @param authentication     the authentication token
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/recipes/{recipeId}/nutritionalValues/{nutritionalValueId}")
    public ResponseEntity<String> deleteNutritionalValue(@PathVariable Long recipeId, @PathVariable Long nutritionalValueId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        nutritionalValueService.deleteNutritionalValue(nutritionalValueId, recipeId);
        return ResponseEntity.ok("Nutritional value deleted successfully.");
    }

    /**
     * Retrieves all nutritional values across all recipes belonging to the authenticated user.
     *
     * @param authentication the authentication token
     * @return a list of all nutritional values across all recipes for the authenticated user
     */
    @GetMapping("/users/nutritionalValues")
    public ResponseEntity<List<NutritionalValue>> getAllNutritionalValuesByUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<NutritionalValue> nutritionalValues = nutritionalValueService.getAllNutritionalValuesByUserId(user.getId());
        return ResponseEntity.ok(nutritionalValues);
    }
}