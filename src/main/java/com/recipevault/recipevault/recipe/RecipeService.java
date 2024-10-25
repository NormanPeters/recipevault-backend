package com.recipevault.recipevault.recipe;

import com.recipevault.recipevault.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Retrieve all recipes for a specific user.
     */
    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }

    /**
     * Create a new recipe for a specific user.
     */
    public Recipe createRecipe(Recipe recipe, User user) {
        recipe.setUser(user);

        // Link ingredients, nutritional values, and steps to the recipe
        recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe));
        recipe.getNutritionalValues().forEach(nutritionalValue -> nutritionalValue.setRecipe(recipe));
        recipe.getSteps().forEach(step -> step.setRecipe(recipe));
        recipe.getTools().forEach(tool -> tool.setRecipe(recipe));

        return recipeRepository.save(recipe);
    }

    /**
     * Retrieve a recipe by its ID.
     */
    public Recipe getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new RuntimeException("Recipe not found for ID: " + id);
        }
    }

    /**
     * Update an existing recipe for a specific user.
     */
    public Recipe updateRecipe(Long id, Recipe recipeDetails, User user) {
        Recipe recipe = getRecipeById(id);

        // Ensure the recipe belongs to the user
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this recipe.");
        }

        recipe.setTitle(recipeDetails.getTitle());
        recipe.setDescription(recipeDetails.getDescription());
        recipe.setImageUrl(recipeDetails.getImageUrl());
        recipe.setFavorite(recipeDetails.getFavorite());
        recipe.setTime(recipeDetails.getTime());
        recipe.setSourceUrl(recipeDetails.getSourceUrl());
        recipe.setServings(recipeDetails.getServings());
        recipe.setPortionSize(recipeDetails.getPortionSize());

        // Clear existing ingredients, nutritional values, and steps before updating
        recipe.getIngredients().clear();
        recipe.getNutritionalValues().clear();
        recipe.getSteps().clear();
        recipe.getTools().clear();

        // Re-add the new ingredients, nutritional values, and steps
        recipeDetails.getIngredients().forEach(recipe::addIngredient);
        recipeDetails.getNutritionalValues().forEach(recipe::addNutritionalValue);
        recipeDetails.getSteps().forEach(recipe::addStep);
        recipeDetails.getTools().forEach(recipe::addTool);

        return recipeRepository.save(recipe);
    }

    /**
     * Delete a recipe for a specific user.
     */
    public void deleteRecipe(Long id, User user) {
        Recipe recipe = getRecipeById(id);

        // Ensure the recipe belongs to the user
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this recipe.");
        }

        recipeRepository.delete(recipe);
    }
}