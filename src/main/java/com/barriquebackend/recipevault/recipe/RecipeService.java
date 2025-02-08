package com.barriquebackend.recipevault.recipe;

import com.barriquebackend.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to recipes.
 * Provides methods for creating, retrieving, updating, and deleting recipes
 * that are associated with a particular user.
 */
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * Constructs a RecipeService with the specified RecipeRepository.
     *
     * @param recipeRepository the repository used to perform CRUD operations on recipes
     */
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Retrieves all recipes for the specified user.
     *
     * @param userId the ID of the user whose recipes are to be retrieved
     * @return a list of recipes belonging to the user
     */
    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to retrieve
     * @return the recipe with the specified ID
     * @throws RuntimeException if no recipe is found with the given ID
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
     * Creates a new recipe for the specified user.
     * <p>
     * This method sets the recipe's owner and links all its components (ingredients,
     * nutritional values, steps, tools, and tags) to the recipe before saving.
     * </p>
     *
     * @param recipe the recipe object to be created
     * @param user   the user who will own the recipe
     * @return the created recipe
     */
    public Recipe createRecipe(Recipe recipe, User user) {
        recipe.setUser(user);

        // Link each recipe component to the recipe
        recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe));
        recipe.getNutritionalValues().forEach(nutritionalValue -> nutritionalValue.setRecipe(recipe));
        recipe.getSteps().forEach(step -> step.setRecipe(recipe));
        recipe.getTools().forEach(tool -> tool.setRecipe(recipe));
        recipe.getTags().forEach(tag -> tag.setRecipe(recipe));

        return recipeRepository.save(recipe);
    }

    /**
     * Updates an existing recipe for the specified user.
     * <p>
     * The method first verifies that the recipe exists and belongs to the user;
     * then it updates the recipe's details and its components.
     * </p>
     *
     * @param id            the ID of the recipe to update
     * @param recipeDetails the updated recipe data
     * @param user          the user attempting to update the recipe
     * @return the updated recipe
     * @throws RuntimeException if the recipe does not belong to the user or is not found
     */
    public Recipe updateRecipe(Long id, Recipe recipeDetails, User user) {
        Recipe recipe = getRecipeById(id);

        // Verify ownership
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

        // Clear existing components and re-add updated ones
        recipe.getIngredients().clear();
        recipe.getNutritionalValues().clear();
        recipe.getSteps().clear();
        recipe.getTools().clear();
        recipe.getTags().clear();

        recipeDetails.getIngredients().forEach(recipe::addIngredient);
        recipeDetails.getNutritionalValues().forEach(recipe::addNutritionalValue);
        recipeDetails.getSteps().forEach(recipe::addStep);
        recipeDetails.getTools().forEach(recipe::addTool);
        recipeDetails.getTags().forEach(recipe::addTag);

        return recipeRepository.save(recipe);
    }

    /**
     * Deletes a recipe for the specified user.
     *
     * @param id   the ID of the recipe to delete
     * @param user the user attempting to delete the recipe
     * @throws RuntimeException if the recipe does not belong to the user or is not found
     */
    public void deleteRecipe(Long id, User user) {
        Recipe recipe = getRecipeById(id);

        // Verify ownership
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this recipe.");
        }

        recipeRepository.delete(recipe);
    }
}