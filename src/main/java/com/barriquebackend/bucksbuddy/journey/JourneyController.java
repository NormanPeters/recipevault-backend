package com.barriquebackend.bucksbuddy.journey;

import com.barriquebackend.user.User;
import com.barriquebackend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing journeys.
 * Provides endpoints to create, retrieve, update, and delete journeys for authenticated users.
 */
@RestController
@RequestMapping("/api")
public class JourneyController {

    private final JourneyService journeyService;
    private final UserRepository userRepository;

    /**
     * Constructs a JourneyController with the specified JourneyService and UserRepository.
     *
     * @param journeyService the service for journey business logic
     * @param userRepository the repository for user data
     */
    public JourneyController(JourneyService journeyService, UserRepository userRepository) {
        this.journeyService = journeyService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all journeys for the authenticated user.
     *
     * @param authentication the authentication token containing user details
     * @return a list of journeys belonging to the authenticated user
     */
    @GetMapping("/user/journey")
    public List<Journey> getJourneysByUserId(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return journeyService.getAllJourneysByUserId(user.getId());
    }

    /**
     * Retrieves a journey by its ID.
     *
     * @param id             the ID of the journey to retrieve
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the journey if found and authorized, or an appropriate error status
     */
    @GetMapping("/journey/{id}")
    public ResponseEntity<Journey> getJourneyById(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Journey journey = journeyService.getJourneyById(id);
        if (!journey.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(journey);
    }

    /**
     * Creates a new journey for the authenticated user.
     *
     * @param journey        the journey data to create
     * @param authentication the authentication token containing user details
     * @return the created journey
     */
    @PostMapping("/journey")
    public Journey createJourney(@RequestBody Journey journey, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return journeyService.createJourney(journey, user);
    }

    /**
     * Updates an existing journey for the authenticated user.
     *
     * @param id             the ID of the journey to update
     * @param journeyDetails the updated journey data
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the updated journey if successful, or an error status
     */
    @PutMapping("/journey/{id}")
    public ResponseEntity<Journey> updateJourney(@PathVariable Long id, @RequestBody Journey journeyDetails, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Journey journey = journeyService.getJourneyById(id);
        if (!journey.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Journey updatedJourney = journeyService.updateJourney(id, journeyDetails, user);
        return ResponseEntity.ok(updatedJourney);
    }

    /**
     * Deletes a journey for the authenticated user.
     *
     * @param id             the ID of the journey to delete
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with a success message if deletion is successful, or an error status
     */
    @DeleteMapping("/journey/{id}")
    public ResponseEntity<String> deleteJourney(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Journey journey = journeyService.getJourneyById(id);
        if (!journey.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        journeyService.deleteJourney(id, user);
        return ResponseEntity.ok("Journey deleted successfully.");
    }

    /**
     * Helper method to extract the authenticated user from the security context.
     *
     * @param authentication the authentication token containing user details
     * @return the authenticated User
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}