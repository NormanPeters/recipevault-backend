package com.recipevault.recipevault.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recipevault.recipevault.ingredient.Ingredient;
import com.recipevault.recipevault.nutritionalValue.NutritionalValue;
import com.recipevault.recipevault.recipeStep.RecipeStep;
import com.recipevault.recipevault.user.User;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String imageUrl;
    private String title;
    private String description;
    private Boolean isFavourite;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<NutritionalValue> nutritionalValues;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeStep> steps;

    // Getters and setters
    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<NutritionalValue> getNutritionalValues() {
        return nutritionalValues;
    }

    public void setNutritionalValues(List<NutritionalValue> nutritionalValues) {
        this.nutritionalValues = nutritionalValues;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
    }

    public void addNutritionalValue(NutritionalValue nutritionalValue) {
        nutritionalValues.add(nutritionalValue);
        nutritionalValue.setRecipe(this);
    }

    public void removeNutritionalValue(NutritionalValue nutritionalValue) {
        nutritionalValues.remove(nutritionalValue);
        nutritionalValue.setRecipe(null);
    }

    public void addStep(RecipeStep step) {
        steps.add(step);
        step.setRecipe(this);
    }

    public void removeStep(RecipeStep step) {
        steps.remove(step);
        step.setRecipe(null);
    }
}
