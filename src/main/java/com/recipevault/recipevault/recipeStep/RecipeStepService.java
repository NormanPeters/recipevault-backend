package com.recipevault.recipevault.recipeStep;

import com.recipevault.recipevault.recipe.Recipe;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

    public RecipeStepService(RecipeStepRepository recipeStepRepository) {
        this.recipeStepRepository = recipeStepRepository;
    }

    /**
     * Creates a new recipe step and associates it with a given recipe.
     *
     * @param recipe     The recipe to which the step will be added.
     * @param recipeStep The step details to be added.
     * @return The created step associated with the recipe.
     */
    public RecipeStep createRecipeStep(Recipe recipe, RecipeStep recipeStep) {
        recipeStep.setRecipe(recipe);
        return recipeStepRepository.save(recipeStep);
    }

    /**
     * Retrieves all recipe steps for a given recipe by its ID.
     *
     * @param recipeId The ID of the recipe whose steps need to be retrieved.
     * @return A list of all steps associated with the specified recipe.
     */
    public List<RecipeStep> getAllRecipeStepsByRecipeId(Long recipeId) {
        return recipeStepRepository.findAllByRecipe_RecipeId(recipeId);
    }

    /**
     * Retrieves a recipe step by its ID and the associated recipe ID.
     *
     * @param stepId    The ID of the step to retrieve.
     * @param recipeId  The ID of the recipe that the step belongs to.
     * @return An Optional containing the step if found, otherwise empty.
     */
    public Optional<RecipeStep> getRecipeStepByIdAndRecipeId(Long stepId, Long recipeId) {
        return Optional.ofNullable(recipeStepRepository.findByStepIdAndRecipe_RecipeId(stepId, recipeId));
    }

    /**
     * Retrieves all recipe steps across all recipes owned by a user.
     *
     * @param userId The ID of the user.
     * @return A list of steps across all user's recipes.
     */
    public List<RecipeStep> getAllRecipeStepsByUserId(Long userId) {
        return recipeStepRepository.findAllByRecipe_User_Id(userId);
    }

    /**
     * Updates the details of an existing recipe step for a given recipe.
     *
     * @param stepId       The ID of the step to be updated.
     * @param recipeId     The ID of the recipe that the step belongs to.
     * @param stepDetails  The new step details to be applied.
     * @return The updated step with the new details.
     */
    public RecipeStep updateRecipeStep(Long stepId, Long recipeId, RecipeStep stepDetails) {
        RecipeStep recipeStep = recipeStepRepository.findByStepIdAndRecipe_RecipeId(stepId, recipeId);

        recipeStep.setStepDescription(stepDetails.getStepDescription());
        recipeStep.setStepNumber(stepDetails.getStepNumber());

        return recipeStepRepository.save(recipeStep);
    }

    /**
     * Deletes a recipe step from a recipe.
     *
     * @param stepId    The ID of the step to be deleted.
     * @param recipeId  The ID of the recipe that the step belongs to.
     */
    public void deleteRecipeStep(Long stepId, Long recipeId) {
        RecipeStep recipeStep = recipeStepRepository.findByStepIdAndRecipe_RecipeId(stepId, recipeId);
        if (recipeStep != null) {
            recipeStepRepository.deleteById(recipeStep.getStepId());
        }
    }
}