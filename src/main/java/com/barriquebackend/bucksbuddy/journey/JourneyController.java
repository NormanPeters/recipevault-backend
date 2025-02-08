package com.barriquebackend.bucksbuddy.journey;

import com.barriquebackend.user.User;
import com.barriquebackend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JourneyController {

    private final JourneyService journeyService;
    private final UserRepository userRepository;

    public JourneyController(JourneyService journeyService, UserRepository userRepository) {
        this.journeyService = journeyService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all journeys for the authenticated user.
     */
    @GetMapping("/user/journey")
    public List<Journey> getJourneysByUserId(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return journeyService.getAllJourneysByUserId(user.getId());
    }

    /**
     * Retrieve a journey by ID.
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
     * Create a new journey for the authenticated user.
     */
    @PostMapping("/journey")
    public Journey createJourney(@RequestBody Journey journey, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return journeyService.createJourney(journey, user);
    }

    /**
     * Update an existing journey for the authenticated user.
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
     * Delete a journey for the authenticated user.
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
     * Helper method to extract the authenticated user from the JWT token.
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}