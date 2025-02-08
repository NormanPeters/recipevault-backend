package com.barriquebackend.bucksbuddy.journey;

import com.barriquebackend.user.User;
import com.barriquebackend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;

    public JourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    /**
     * Retrieve all journeys for a specific user.
     */
    public List<Journey> getAllJourneysByUserId(Long userId) {
        return journeyRepository.findAllByUserId(userId);
    }

    /**
     * Retrieve a journey by its ID.
     */
    public Journey getJourneyById(Long id) {
        Optional<Journey> journey = journeyRepository.findById(id);
        if (journey.isPresent()) {
            return journey.get();
        } else {
            throw new RuntimeException("Journey not found for ID: " + id);
        }
    }

    /**
     * Create a new journey for a specific user.
     */
    public Journey createJourney(Journey journey, User user) {
        journey.setUser(user);
        return journeyRepository.save(journey);
    }

    /**
     * Update an existing journey for the given user.
     */
    public Journey updateJourney(Long id, Journey journeyDetails, User user) {
        Journey journey = getJourneyById(id);

        // Ensure the journey belongs to the authenticated user
        if (!journey.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this journey.");
        }

        journey.setName(journeyDetails.getName());
        journey.setHomeCurr(journeyDetails.getHomeCurr());
        journey.setVacCurr(journeyDetails.getVacCurr());
        journey.setBudget(journeyDetails.getBudget());
        journey.setStartDate(journeyDetails.getStartDate());
        journey.setEndDate(journeyDetails.getEndDate());

        return journeyRepository.save(journey);
    }

    /**
     * Delete a journey by its ID.
     */
    public void deleteJourney(Long id, User user) {
        Journey journey = getJourneyById(id);

        // Ensure the journey belongs to the authenticated user
        if (!journey.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this journey.");
        }

        journeyRepository.deleteById(id);
    }
}