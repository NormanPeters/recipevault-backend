package com.recipevault.recipevault.recipe.recipeComponents.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recipevault.recipevault.recipe.Recipe;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private List<Recipe> recipes;

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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
