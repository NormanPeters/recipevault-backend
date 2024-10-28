package com.recipevault.recipevault.recipe.recipeComponents.tag;

public enum TagType {
    // Nutrition tags
    MEAT("Nutrition"),
    FISH("Nutrition"),
    VEGETARIAN("Nutrition"),
    VEGAN("Nutrition"),

    // Difficulty tags
    EASY("Difficulty"),
    MEDIUM("Difficulty"),
    HARD("Difficulty"),

    // Meal Type tags
    APPETIZER("Meal Type"),
    MAIN_COURSE("Meal Type"),
    DESSERT("Meal Type"),
    BREAKFAST("Meal Type"),
    SOUP("Meal Type"),
    CASSEROLE("Meal Type"),
    SNACK("Meal Type"),
    BEVERAGE("Meal Type"),

    // Diet Type tags
    LACTOSE_FREE("Diet Type"),
    LOW_CARB("Diet Type"),
    GLUTEN_FREE("Diet Type"),
    PALEO("Diet Type"),
    LOW_SUGAR("Diet Type"),
    CLEAN_EATING("Diet Type");

    private final String category;

    TagType(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

