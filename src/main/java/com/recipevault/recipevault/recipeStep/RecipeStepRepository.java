package com.recipevault.recipevault.recipeStep;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    // Find a step by its ID and the associated recipe's ID
    RecipeStep findByStepIdAndRecipe_RecipeId(Long stepId, Long recipeId);

    // Get all steps by the recipe ID
    List<RecipeStep> findAllByRecipe_RecipeId(Long recipeId);

    // Get all steps by the user ID via recipe ownership
    List<RecipeStep> findAllByRecipe_User_Id(Long userId);
}