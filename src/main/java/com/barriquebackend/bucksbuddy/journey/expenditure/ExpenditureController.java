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

@RestController
@RequestMapping("/api")
public class ExpenditureController {

    private final ExpenditureService expenditureService;
    private final JourneyService journeyService;
    private final UserRepository userRepository;

    @Autowired
    public ExpenditureController(ExpenditureService expenditureService, JourneyService journeyService, UserRepository userRepository) {
        this.expenditureService = expenditureService;
        this.journeyService = journeyService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all expenditures for a given journey.
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
     * Retrieve an expenditure by its ID.
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
     * Create a new expenditure for a given journey.
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
     * Update an existing expenditure.
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
     * Delete an expenditure.
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
     * Helper method to extract the authenticated user.
     */
    private User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    /**
     * Helper method to validate journey ownership.
     * Returns the Journey if the authenticated user owns it; otherwise, returns an empty Optional.
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