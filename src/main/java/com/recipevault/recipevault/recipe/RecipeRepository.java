package com.recipevault.recipevault.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByTitleAndUserId(String title, Long userId);
    Recipe findByTitle(String title);
    List<Recipe> findAllByUserId(Long userId);
}