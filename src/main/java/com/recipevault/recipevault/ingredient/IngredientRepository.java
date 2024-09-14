package com.recipevault.recipevault.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // Find ingredient by its ID and the associated recipe's ID
    Ingredient findByIngredientIdAndRecipe_RecipeId(Long ingredientId, Long recipeId);

    // Get all ingredients by the user ID via recipe ownership
    List<Ingredient> findAllByRecipe_User_Id(Long userId);
}