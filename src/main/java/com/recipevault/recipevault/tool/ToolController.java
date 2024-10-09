package com.recipevault.recipevault.tool;

import com.recipevault.recipevault.recipe.Recipe;
import com.recipevault.recipevault.recipe.RecipeService;
import com.recipevault.recipevault.user.User;
import com.recipevault.recipevault.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToolController {

    private final ToolService toolService;
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    public ToolController(ToolService toolService, RecipeService recipeService, UserRepository userRepository) {
        this.toolService = toolService;
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    /**
     * Create a new tool and add it to a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param tool           the tool to be added
     * @param authentication the authentication token
     * @return the created tool
     */
    @PostMapping("/recipes/{recipeId}/tools")
    public ResponseEntity<Tool> createTool(@PathVariable Long recipeId, @RequestBody Tool tool, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Tool createdTool = toolService.createTool(recipe, tool);
        return ResponseEntity.ok(createdTool);
    }

    /**
     * Retrieve all tools for a given recipe by its ID.
     *
     * @param recipeId       the ID of the recipe whose tools need to be retrieved
     * @param authentication the authentication token
     * @return a list of all tools associated with the specified recipe
     */
    @GetMapping("/recipes/{recipeId}/tools")
    public ResponseEntity<List<Tool>> getAllToolsByRecipe(@PathVariable Long recipeId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<Tool> tools = toolService.getAllToolsByRecipeId(recipeId);
        return ResponseEntity.ok(tools);
    }

    /**
     * Retrieve a tool by its ID and the associated recipe ID.
     *
     * @param recipeId       the ID of the recipe
     * @param toolId         the ID of the tool
     * @param authentication the authentication token
     * @return the tool if found, otherwise 404 Not Found
     */
    @GetMapping("/recipes/{recipeId}/tools/{toolId}")
    public ResponseEntity<Tool> getToolById(@PathVariable Long recipeId, @PathVariable Long toolId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Optional<Tool> tool = toolService.getToolByIdAndRecipeId(toolId, recipeId);
        return tool.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update the details of an existing tool for a given recipe.
     *
     * @param recipeId       the ID of the recipe
     * @param toolId         the ID of the tool
     * @param toolDetails    the updated tool details
     * @param authentication the authentication token
     * @return the updated tool
     */
    @PutMapping("/recipes/{recipeId}/tools/{toolId}")
    public ResponseEntity<Tool> updateTool(@PathVariable Long recipeId, @PathVariable Long toolId, @RequestBody Tool toolDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Tool updatedTool = toolService.updateTool(toolId, recipeId, toolDetails);
        return ResponseEntity.ok(updatedTool);
    }

    /**
     * Delete a tool from a recipe for the authenticated user.
     *
     * @param recipeId       the ID of the recipe
     * @param toolId         the ID of the tool
     * @param authentication the authentication token
     * @return a success message
     */
    @DeleteMapping("/recipes/{recipeId}/tools/{toolId}")
    public ResponseEntity<String> deleteTool(@PathVariable Long recipeId, @PathVariable Long toolId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (!recipe.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        toolService.deleteTool(toolId, recipeId);
        return ResponseEntity.ok("Tool deleted successfully.");
    }
}