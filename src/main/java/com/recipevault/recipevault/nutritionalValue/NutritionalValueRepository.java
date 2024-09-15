package com.recipevault.recipevault.nutritionalValue;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NutritionalValueRepository extends JpaRepository<NutritionalValue, Long> {

    // Find nutritional value by its ID and the associated recipe's ID
    NutritionalValue findByNutritionalValueIdAndRecipe_RecipeId(Long nutritionalValueId, Long recipeId);

    // Get all nutritional values by the recipe ID
    List<NutritionalValue> findAllByRecipe_RecipeId(Long recipeId);

    // Get all nutritional values by the user ID via recipe ownership
    List<NutritionalValue> findAllByRecipe_User_Id(Long userId);
}