package com.recipevault.recipevault.recipe.recipeComponents;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recipevault.recipevault.recipe.Recipe;
import jakarta.persistence.*;

@Entity
public class NutritionalValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutritionalValueId;

    private String title;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;

    // Getters and setters
    public Long getNutritionalValueId() {
        return nutritionalValueId;
    }

    public void setNutritionalValueId(Long nutritionalValueId) {
        this.nutritionalValueId = nutritionalValueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
