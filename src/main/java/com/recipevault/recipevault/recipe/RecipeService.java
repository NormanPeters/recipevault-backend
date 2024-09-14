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
     * Create a new recipe for a specific user.
     */
    public Recipe createRecipe(Recipe recipe, User user) {
        recipe.setUser(user);
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
     * Retrieve all recipes for a specific user.
     */
    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
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
        recipe.setFavourite(recipeDetails.getFavourite());
        recipe.setIngredients(recipeDetails.getIngredients());
        recipe.setNutritionalValues(recipeDetails.getNutritionalValues());
        recipe.setSteps(recipeDetails.getSteps());

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

    /**
     * Search for a recipe by title for the specific user.
     */
    public Optional<Recipe> findByTitleForUser(String title, User user) {
        return recipeRepository.findByTitleAndUserId(title, user.getId());
    }
}