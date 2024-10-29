package com.recipevault.recipevault.recipe.recipeComponents.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recipevault.recipevault.recipe.Recipe;
import jakarta.persistence.*;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonBackReference // Prevents infinite recursion in JSON serialization
    private Recipe recipe;

    // Getters and Setters
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
