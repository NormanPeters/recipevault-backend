package com.recipevault.recipevault.tool;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToolRepository extends JpaRepository<Tool, Long> {

    Tool findByToolIdAndRecipe_RecipeId(Long toolId, Long recipeId);

    List<Tool> findAllByRecipe_RecipeId(Long recipeId);
}