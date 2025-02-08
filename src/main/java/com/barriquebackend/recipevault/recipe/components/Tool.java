package com.barriquebackend.recipevault.recipe.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.barriquebackend.recipevault.recipe.Recipe;
import jakarta.persistence.*;

@Entity
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toolId;

    private String title;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonBackReference // Prevents infinite recursion during serialization
    private Recipe recipe;

    // Getters and Setters
    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}