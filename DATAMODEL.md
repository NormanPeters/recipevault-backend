### 1. **User to Recipe: One-to-Many Relationship**
- **User** table contains attributes:
    - `user_id`: Primary key, uniquely identifies each user.
    - `username`: User's chosen username.
    - `password`: User's hashed password.
    - `email`: User's email address, must be unique.
    - **One-to-many** relationship: One user can have multiple recipes.

- **Recipe** table contains attributes:
    - `recipe_id`: Primary key, uniquely identifies each recipe.
    - `user_id`: Foreign key, refers to the user who created the recipe.
    - `image_url`: URL of the recipe's image.
    - `title`: Title of the recipe.
    - `description`: Detailed description of the recipe.
    - `is_favourite`: Boolean, marks whether the recipe is a favorite of the user.
    - `created_at`: Timestamp, the time the recipe was created.
    - `updated_at`: Timestamp, the time the recipe was last updated.

**Relationship:**

```sql
-- In the Recipe table, user_id is a foreign key referencing User table
ALTER TABLE Recipe ADD CONSTRAINT fk_user_recipe
FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE;
```

### 2. **Recipe to Ingredients: One-to-Many Relationship**
- **Recipe** table attributes (already listed above).

- **Ingredient** table contains attributes:
    - `ingredient_id`: Primary key, uniquely identifies each ingredient.
    - `recipe_id`: Foreign key, refers to the recipe to which this ingredient belongs.
    - `title`: Name of the ingredient.
    - `amount`: Quantity of the ingredient.
    - `unit`: Unit of measurement for the ingredient (e.g., grams, liters).

**Relationship:**

```sql
-- In the Ingredient table, recipe_id is a foreign key referencing Recipe table
ALTER TABLE Ingredient ADD CONSTRAINT fk_recipe_ingredient
FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;
```

### 3. **Recipe to Nutritional Values: One-to-Many Relationship**
- **Recipe** table attributes (already listed above).

- **NutritionalValue** table contains attributes:
    - `nutritional_value_id`: Primary key, uniquely identifies each nutritional value entry.
    - `recipe_id`: Foreign key, refers to the recipe this nutritional value is associated with.
    - `title`: Name of the nutritional value (e.g., calories, proteins, fats).
    - `amount`: Amount of the nutritional value (e.g., 100 calories).

**Relationship:**

```sql
-- In the NutritionalValue table, recipe_id is a foreign key referencing Recipe table
ALTER TABLE NutritionalValue ADD CONSTRAINT fk_recipe_nutritionalvalue
FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;
```

### 4. **Recipe to RecipeStep: One-to-Many Relationship**
- **Recipe** table attributes (already listed above).

- **RecipeStep** table contains attributes:
    - `step_id`: Primary key, uniquely identifies each step.
    - `recipe_id`: Foreign key, refers to the recipe this step belongs to.
    - `step_number`: The sequence number of the step in the recipe.
    - `step_description`: Detailed instruction for this step.

**Relationship:**

```sql
-- In the RecipeStep table, recipe_id is a foreign key referencing Recipe table
ALTER TABLE RecipeStep ADD CONSTRAINT fk_recipe_step
FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;
```

### Final Database Model with Relationships

```sql
CREATE TABLE User (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Recipe (
    recipe_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES User(user_id) ON DELETE CASCADE,
    image_url TEXT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_favourite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Ingredient (
    ingredient_id SERIAL PRIMARY KEY,
    recipe_id INT REFERENCES Recipe(recipe_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(50)
);

CREATE TABLE NutritionalValue (
    nutritional_value_id SERIAL PRIMARY KEY,
    recipe_id INT REFERENCES Recipe(recipe_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 4)
);

CREATE TABLE RecipeStep (
    step_id SERIAL PRIMARY KEY,
    recipe_id INT REFERENCES Recipe(recipe_id) ON DELETE CASCADE,
    step_number INT NOT NULL,
    step_description TEXT NOT NULL
);
```

### Summary of Relationships and Attributes:
- **User** has a `One-to-Many` relationship with `Recipe` (each user can have multiple recipes).
- **Recipe** has a `One-to-Many` relationship with `Ingredient` (each recipe can have multiple ingredients).
- **Recipe** has a `One-to-Many` relationship with `NutritionalValue` (each recipe can have multiple nutritional values).
- **Recipe** has a `One-to-Many` relationship with `RecipeStep` (each recipe can have multiple steps).

All foreign key constraints are set with `ON DELETE CASCADE` to maintain referential integrity, ensuring that associated records are deleted when a parent record is removed.