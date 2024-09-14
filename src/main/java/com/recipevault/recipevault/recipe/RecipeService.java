package com.recipevault.recipevault.recipe;

import com.recipevault.recipevault.ingredient.Ingredient;
import com.recipevault.recipevault.nutritionalValue.NutritionalValue;
import com.recipevault.recipevault.recipeStep.RecipeStep;
import com.recipevault.recipevault.user.UserRepository;
import com.recipevault.recipevault.user.User;

import java.util.List;

public class RecipeService {

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    public RecipeService(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Get all recipes by a user's ID
     * @param userId the ID of the user whose recipes are to be retrieved
     * @return List of all recipes by the user with the given ID
     */
    public List<Recipe> getAllRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserID(userId);
    }

    /**
     * Get a recipe by its ID
     * @param id the ID of the recipe to be retrieved
     * @return the recipe with the given ID
     */
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new recipe for a specific user
     *
     * @param userId       The ID of the user
     * @param title        The title of the recipe
     * @param description  The description of the recipe
     * @param imageUrl     The image URL of the recipe
     * @param isFavourite  Whether the recipe is marked as a favorite
     * @param ingredients  The list of ingredients
     * @param nutritionalValues The list of nutritional values
     * @param steps        The list of preparation steps
     * @return The saved recipe
     */
    public Recipe saveRecipe(Long userId, String title, String description, String imageUrl, Boolean isFavourite, List<Ingredient> ingredients, List<NutritionalValue> nutritionalValues, List<RecipeStep> steps) {
        // Retrieve the user
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new recipe
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setImageUrl(imageUrl);
        recipe.setFavourite(isFavourite);
        recipe.setIngredients(ingredients);
        recipe.setNutritionalValues(nutritionalValues);
        recipe.setSteps(steps);

        // Associate all ingredients, nutritional values, and steps with the recipe
        ingredients.forEach(ingredient -> ingredient.setRecipe(recipe));
        nutritionalValues.forEach(nutritionalValue -> nutritionalValue.setRecipe(recipe));
        steps.forEach(step -> step.setRecipe(recipe));

        // Save the recipe
        return recipeRepository.save(recipe);
    }


}
