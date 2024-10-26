package com.recipevault.recipevault.recipe.recipeComponents;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recipevault.recipevault.recipe.Recipe;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String title;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private List<Recipe> recipes;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
