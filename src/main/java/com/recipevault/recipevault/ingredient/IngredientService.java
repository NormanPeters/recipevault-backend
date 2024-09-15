package com.recipevault.recipevault.ingredient;

import com.recipevault.recipevault.recipe.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Creates a new ingredient and associates it with a given recipe.
     *
     * @param recipe     The recipe to which the ingredient will be added.
     * @param ingredient The ingredient details to be added.
     * @return The created ingredient associated with the recipe.
     */
    public Ingredient createIngredient(Recipe recipe, Ingredient ingredient) {
        ingredient.setRecipe(recipe);
        return ingredientRepository.save(ingredient);
    }

    /**
     * Retrieves all ingredients for a given recipe by its ID.
     *
     * @param recipeId The ID of the recipe whose ingredients need to be retrieved.
     * @return A list of all ingredients associated with the specified recipe.
     */
    public List<Ingredient> getAllIngredientsByRecipeId(Long recipeId) {
        return ingredientRepository.findAllByRecipe_RecipeId(recipeId);
    }

    /**
     * Retrieves an ingredient by its ID and the associated recipe ID.
     *
     * @param ingredientId The ID of the ingredient to retrieve.
     * @param recipeId     The ID of the recipe that the ingredient belongs to.
     * @return An Optional containing the ingredient if found, otherwise empty.
     */
    public Optional<Ingredient> getIngredientByIdAndRecipeId(Long ingredientId, Long recipeId) {
        return Optional.ofNullable(ingredientRepository.findByIngredientIdAndRecipe_RecipeId(ingredientId, recipeId));
    }

    /**
     * Retrieves all ingredients across all recipes owned by a user.
     *
     * @param userId The ID of the user.
     * @return A list of ingredients across all user's recipes.
     */
    public List<Ingredient> getAllIngredientsByUserId(Long userId) {
        return ingredientRepository.findAllByRecipe_User_Id(userId);
    }

    /**
     * Updates the details of an existing ingredient for a given recipe.
     *
     * @param ingredientId     The ID of the ingredient to be updated.
     * @param recipeId         The ID of the recipe that the ingredient belongs to.
     * @param ingredientDetails The new ingredient details to be applied.
     * @return The updated ingredient with the new details.
     */
    public Ingredient updateIngredient(Long ingredientId, Long recipeId, Ingredient ingredientDetails) {
        Ingredient ingredient = ingredientRepository.findByIngredientIdAndRecipe_RecipeId(ingredientId, recipeId);

        ingredient.setTitle(ingredientDetails.getTitle());
        ingredient.setAmount(ingredientDetails.getAmount());
        ingredient.setUnit(ingredientDetails.getUnit());

        return ingredientRepository.save(ingredient);
    }

    /**
     * Deletes an ingredient from a recipe.
     *
     * @param ingredientId The ID of the ingredient to be deleted.
     * @param recipeId     The ID of the recipe that the ingredient belongs to.
     */
    public void deleteIngredient(Long ingredientId, Long recipeId) {
        Ingredient ingredient = ingredientRepository.findByIngredientIdAndRecipe_RecipeId(ingredientId, recipeId);
        if (ingredient != null) {
            ingredientRepository.deleteById(ingredient.getIngredientId());
        }
    }
}