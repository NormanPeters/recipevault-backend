package com.recipevault.recipevault.recipeStep;

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
public class RecipeStepController {

    private final RecipeStepService recipeStepService;
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    public RecipeStepController(RecipeStepService recipeStepService, RecipeService recipeService, UserRepository userRepository) {
        this.recipeStepService = recipeStepService;
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
     * Create a new recipe step and add it to a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param recipeStep     the step to be added
     * @param authentication the authentication token
     * @return the created step
     */
    @PostMapping("/recipes/{recipeId}/steps")
    public ResponseEntity<RecipeStep> createRecipeStep(@PathVariable Long recipeId, @RequestBody RecipeStep recipeStep, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        RecipeStep createdStep = recipeStepService.createRecipeStep(recipe, recipeStep);
        return ResponseEntity.ok(createdStep);
    }

    /**
     * Find all steps for a specific recipe.
     *
     * @param recipeId       the ID of the recipe
     * @param authentication the authentication token
     * @return a list of steps for the specified recipe
     */
    @GetMapping("/recipes/{recipeId}/steps")
    public ResponseEntity<List<RecipeStep>> getAllStepsByRecipe(@PathVariable Long recipeId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<RecipeStep> steps = recipeStepService.getAllRecipeStepsByRecipeId(recipeId);
        return ResponseEntity.ok(steps);
    }

    /**
     * Find a step by its ID and recipe.
     *
     * @param recipeId       the ID of the recipe
     * @param stepId         the ID of the step
     * @param authentication the authentication token
     * @return the step if found, otherwise a 404 response
     */
    @GetMapping("/recipes/{recipeId}/steps/{stepId}")
    public ResponseEntity<RecipeStep> getRecipeStepById(@PathVariable Long recipeId, @PathVariable Long stepId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Optional<RecipeStep> recipeStep = recipeStepService.getRecipeStepByIdAndRecipeId(stepId, recipeId);
        return recipeStep.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a step in a specific recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param stepId         the ID of the step
     * @param stepDetails    the updated step details
     * @param authentication the authentication token
     * @return the updated step
     */
    @PutMapping("/recipes/{recipeId}/steps/{stepId}")
    public ResponseEntity<RecipeStep> updateRecipeStep(@PathVariable Long recipeId, @PathVariable Long stepId, @RequestBody RecipeStep stepDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        RecipeStep updatedStep = recipeStepService.updateRecipeStep(stepId, recipeId, stepDetails);
        return ResponseEntity.ok(updatedStep);
    }

    /**
     * Delete a step from a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param stepId         the ID of the step
     * @param authentication the authentication token
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/recipes/{recipeId}/steps/{stepId}")
    public ResponseEntity<String> deleteRecipeStep(@PathVariable Long recipeId, @PathVariable Long stepId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        recipeStepService.deleteRecipeStep(stepId, recipeId);
        return ResponseEntity.ok("Step deleted successfully.");
    }

    /**
     * Retrieves all steps across all recipes belonging to the authenticated user.
     *
     * @param authentication the authentication token
     * @return a list of all steps across all recipes for the authenticated user
     */
    @GetMapping("/users/steps")
    public ResponseEntity<List<RecipeStep>> getAllStepsByUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<RecipeStep> recipeSteps = recipeStepService.getAllRecipeStepsByUserId(user.getId());
        return ResponseEntity.ok(recipeSteps);
    }
}