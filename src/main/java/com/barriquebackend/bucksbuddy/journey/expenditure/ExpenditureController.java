package com.barriquebackend.bucksbuddy.journey.expenditure;

import com.barriquebackend.bucksbuddy.journey.Journey;
import com.barriquebackend.bucksbuddy.journey.JourneyService;
import com.barriquebackend.user.User;
import com.barriquebackend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing expenditures related to journeys.
 * Provides endpoints to create, retrieve, update, and delete expenditures.
 */
@RestController
@RequestMapping("/api")
public class ExpenditureController {

    private final ExpenditureService expenditureService;
    private final JourneyService journeyService;
    private final UserRepository userRepository;

    /**
     * Constructs an ExpenditureController with the specified services and user repository.
     *
     * @param expenditureService the service for expenditure business logic
     * @param journeyService     the service for journey business logic
     * @param userRepository     the repository for user data
     */
    @Autowired
    public ExpenditureController(ExpenditureService expenditureService, JourneyService journeyService, UserRepository userRepository) {
        this.expenditureService = expenditureService;
        this.journeyService = journeyService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all expenditures for a given journey.
     *
     * @param journeyId      the ID of the journey
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the list of expenditures if authorized, or an error status
     */
    @GetMapping("/journey/{journeyId}/expenditure")
    public ResponseEntity<List<Expenditure>> getAllExpendituresByJourneyId(@PathVariable Long journeyId,
                                                                           Authentication authentication) {
        Optional<Journey> authorizedJourney = getAuthorizedJourney(journeyId, authentication);
        if (authorizedJourney.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = getAuthenticatedUser(authentication);
        List<Expenditure> expenditures = expenditureService.getAllExpendituresByJourneyId(journeyId, user.getId());
        return ResponseEntity.ok(expenditures);
    }

    /**
     * Retrieves an expenditure by its ID for a given journey.
     *
     * @param journeyId      the ID of the journey
     * @param expenditureId  the ID of the expenditure
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the expenditure if found and authorized, or an error status
     */
    @GetMapping("/journey/{journeyId}/expenditure/{expenditureId}")
    public ResponseEntity<Expenditure> getExpenditureById(@PathVariable Long journeyId,
                                                          @PathVariable Long expenditureId,
                                                          Authentication authentication) {
        Optional<Journey> authorizedJourney = getAuthorizedJourney(journeyId, authentication);
        if (authorizedJourney.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Expenditure> expenditureOpt = expenditureService.getExpenditureById(expenditureId);
        if (expenditureOpt.isPresent() && expenditureOpt.get().getJourney().getJourneyId().equals(journeyId)) {
            return ResponseEntity.ok(expenditureOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new expenditure for a given journey.
     *
     * @param journeyId      the ID of the journey
     * @param expenditure    the expenditure data to create
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the created expenditure if successful, or an error status
     */
    @PostMapping("/journey/{journeyId}/expenditure")
    public ResponseEntity<Expenditure> createExpenditure(@PathVariable Long journeyId,
                                                         @RequestBody Expenditure expenditure,
                                                         Authentication authentication) {
        Optional<Journey> authorizedJourney = getAuthorizedJourney(journeyId, authentication);
        if (authorizedJourney.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Expenditure createdExpenditure = expenditureService.createExpenditure(journeyId, expenditure);
        return new ResponseEntity<>(createdExpenditure, HttpStatus.CREATED);
    }

    /**
     * Updates an existing expenditure for a given journey.
     *
     * @param journeyId      the ID of the journey
     * @param expenditureId  the ID of the expenditure to update
     * @param expenditure    the updated expenditure data
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with the updated expenditure if successful, or an error status
     */
    @PutMapping("/journey/{journeyId}/expenditure/{expenditureId}")
    public ResponseEntity<Expenditure> updateExpenditure(@PathVariable Long journeyId,
                                                         @PathVariable Long expenditureId,
                                                         @RequestBody Expenditure expenditure,
                                                         Authentication authentication) {
        Optional<Journey> authorizedJourney = getAuthorizedJourney(journeyId, authentication);
        if (authorizedJourney.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Expenditure> existingExp = expenditureService.getExpenditureById(expenditureId);
        if (existingExp.isPresent() && existingExp.get().getJourney().getJourneyId().equals(journeyId)) {
            Optional<Expenditure> updatedExp = expenditureService.updateExpenditure(expenditureId, expenditure);
            return updatedExp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an expenditure for a given journey.
     *
     * @param journeyId      the ID of the journey
     * @param expenditureId  the ID of the expenditure to delete
     * @param authentication the authentication token containing user details
     * @return a ResponseEntity with no content if deletion is successful, or an error status
     */
    @DeleteMapping("/journey/{journeyId}/expenditure/{expenditureId}")
    public ResponseEntity<Void> deleteExpenditure(@PathVariable Long journeyId,
                                                  @PathVariable Long expenditureId,
                                                  Authentication authentication) {
        Optional<Journey> authorizedJourney = getAuthorizedJourney(journeyId, authentication);
        if (authorizedJourney.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Expenditure> expenditureOpt = expenditureService.getExpenditureById(expenditureId);
        if (expenditureOpt.isPresent() && expenditureOpt.get().getJourney().getJourneyId().equals(journeyId)) {
            boolean deleted = expenditureService.deleteExpenditure(expenditureId);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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

    /**
     * Helper method to verify that the specified journey is owned by the authenticated user.
     *
     * @param journeyId      the ID of the journey to verify
     * @param authentication the authentication token containing user details
     * @return an Optional containing the journey if ownership is confirmed, or an empty Optional otherwise
     */
    private Optional<Journey> getAuthorizedJourney(Long journeyId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Journey journey = journeyService.getJourneyById(journeyId);
        if (!journey.getUser().getId().equals(user.getId())) {
            return Optional.empty();
        }
        return Optional.of(journey);
    }
}