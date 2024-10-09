package com.recipevault.recipevault.tool;

import com.recipevault.recipevault.recipe.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * Creates a new tool and associates it with a given recipe.
     *
     * @param recipe The recipe to which the tool will be added.
     * @param tool   The tool details to be added.
     * @return The created tool associated with the recipe.
     */
    public Tool createTool(Recipe recipe, Tool tool) {
        tool.setRecipe(recipe);
        return toolRepository.save(tool);
    }

    /**
     * Retrieves all tools for a given recipe by its ID.
     *
     * @param recipeId The ID of the recipe whose tools need to be retrieved.
     * @return A list of all tools associated with the specified recipe.
     */
    public List<Tool> getAllToolsByRecipeId(Long recipeId) {
        return toolRepository.findAllByRecipe_RecipeId(recipeId);
    }

    /**
     * Retrieves a tool by its ID and the associated recipe ID.
     *
     * @param toolId   The ID of the tool to retrieve.
     * @param recipeId The ID of the recipe that the tool belongs to.
     * @return An Optional containing the tool if found, otherwise empty.
     */
    public Optional<Tool> getToolByIdAndRecipeId(Long toolId, Long recipeId) {
        return Optional.ofNullable(toolRepository.findByToolIdAndRecipe_RecipeId(toolId, recipeId));
    }

    /**
     * Update a tool for a given recipe by its ID.
     *
     * @param toolId      The ID of the tool to update.
     * @param recipeId    The ID of the recipe that the tool belongs to.
     * @param toolDetails The updated tool details.
     * @return The updated tool.
     */
    public Tool updateTool(Long toolId, Long recipeId, Tool toolDetails) {
        Tool tool = toolRepository.findByToolIdAndRecipe_RecipeId(toolId, recipeId);

        tool.setTitle(toolDetails.getTitle());
        tool.setAmount(toolDetails.getAmount());

        return toolRepository.save(tool);
    }

    /**
     * Delete a tool for a given recipe by its ID.
     *
     * @param toolId   The ID of the tool to delete.
     * @param recipeId The ID of the recipe that the tool belongs to.
     */
    public void deleteTool(Long toolId, Long recipeId) {
        Tool tool = toolRepository.findByToolIdAndRecipe_RecipeId(toolId, recipeId);
        if (tool != null) {
            toolRepository.deleteById(tool.getToolId());
        }
    }
}