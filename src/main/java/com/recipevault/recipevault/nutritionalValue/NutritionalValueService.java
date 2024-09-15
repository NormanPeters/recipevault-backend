package com.recipevault.recipevault.nutritionalValue;

import com.recipevault.recipevault.recipe.Recipe;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NutritionalValueService {

    private final NutritionalValueRepository nutritionalValueRepository;

    public NutritionalValueService(NutritionalValueRepository nutritionalValueRepository) {
        this.nutritionalValueRepository = nutritionalValueRepository;
    }

    /**
     * Creates a new nutritional value and associates it with a given recipe.
     *
     * @param recipe            The recipe to which the nutritional value will be added.
     * @param nutritionalValue  The nutritional value details to be added.
     * @return The created nutritional value associated with the recipe.
     */
    public NutritionalValue createNutritionalValue(Recipe recipe, NutritionalValue nutritionalValue) {
        nutritionalValue.setRecipe(recipe);
        return nutritionalValueRepository.save(nutritionalValue);
    }

    /**
     * Retrieves all nutritional values for a given recipe by its ID.
     *
     * @param recipeId The ID of the recipe whose nutritional values need to be retrieved.
     * @return A list of all nutritional values associated with the specified recipe.
     */
    public List<NutritionalValue> getAllNutritionalValuesByRecipeId(Long recipeId) {
        return nutritionalValueRepository.findAllByRecipe_RecipeId(recipeId);
    }

    /**
     * Retrieves a nutritional value by its ID and the associated recipe ID.
     *
     * @param nutritionalValueId The ID of the nutritional value to retrieve.
     * @param recipeId           The ID of the recipe that the nutritional value belongs to.
     * @return An Optional containing the nutritional value if found, otherwise empty.
     */
    public Optional<NutritionalValue> getNutritionalValueByIdAndRecipeId(Long nutritionalValueId, Long recipeId) {
        return Optional.ofNullable(nutritionalValueRepository.findByNutritionalValueIdAndRecipe_RecipeId(nutritionalValueId, recipeId));
    }

    /**
     * Retrieves all nutritional values across all recipes owned by a user.
     *
     * @param userId The ID of the user.
     * @return A list of nutritional values across all user's recipes.
     */
    public List<NutritionalValue> getAllNutritionalValuesByUserId(Long userId) {
        return nutritionalValueRepository.findAllByRecipe_User_Id(userId);
    }

    /**
     * Updates the details of an existing nutritional value for a given recipe.
     *
     * @param nutritionalValueId  The ID of the nutritional value to be updated.
     * @param recipeId            The ID of the recipe that the nutritional value belongs to.
     * @param nutritionalDetails  The new nutritional value details to be applied.
     * @return The updated nutritional value with the new details.
     */
    public NutritionalValue updateNutritionalValue(Long nutritionalValueId, Long recipeId, NutritionalValue nutritionalDetails) {
        NutritionalValue nutritionalValue = nutritionalValueRepository.findByNutritionalValueIdAndRecipe_RecipeId(nutritionalValueId, recipeId);

        nutritionalValue.setTitle(nutritionalDetails.getTitle());
        nutritionalValue.setAmount(nutritionalDetails.getAmount());

        return nutritionalValueRepository.save(nutritionalValue);
    }

    /**
     * Deletes a nutritional value from a recipe.
     *
     * @param nutritionalValueId The ID of the nutritional value to be deleted.
     * @param recipeId           The ID of the recipe that the nutritional value belongs to.
     */
    public void deleteNutritionalValue(Long nutritionalValueId, Long recipeId) {
        NutritionalValue nutritionalValue = nutritionalValueRepository.findByNutritionalValueIdAndRecipe_RecipeId(nutritionalValueId, recipeId);
        if (nutritionalValue != null) {
            nutritionalValueRepository.deleteById(nutritionalValue.getNutritionalValueId());
        }
    }
}