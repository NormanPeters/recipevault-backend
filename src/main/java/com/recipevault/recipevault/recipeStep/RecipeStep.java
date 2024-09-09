package com.recipevault.recipevault.recipeStep;

import com.recipevault.recipevault.recipe.Recipe;
import jakarta.persistence.*;

@Entity
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    private String stepDescription;
    private Integer stepNumber;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    // Getters and setters

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
