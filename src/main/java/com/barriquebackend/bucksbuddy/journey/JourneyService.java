package com.barriquebackend.bucksbuddy.journey;

import com.barriquebackend.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to journeys.
 * Provides methods for creating, retrieving, updating, and deleting journeys
 * associated with a particular user.
 */
@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;

    /**
     * Constructs a JourneyService with the specified JourneyRepository.
     *
     * @param journeyRepository the repository used to perform CRUD operations on journeys
     */
    public JourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    /**
     * Retrieves all journeys for a specific user.
     *
     * @param userId the ID of the user whose journeys are to be retrieved
     * @return a list of journeys belonging to the user
     */
    public List<Journey> getAllJourneysByUserId(Long userId) {
        return journeyRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves a journey by its ID.
     *
     * @param id the ID of the journey to retrieve
     * @return the journey with the specified ID
     * @throws RuntimeException if no journey is found with the given ID
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
     * Creates a new journey for a specific user.
     *
     * @param journey the journey object to be created
     * @param user    the user who will own the journey
     * @return the created journey
     */
    public Journey createJourney(Journey journey, User user) {
        journey.setUser(user);
        return journeyRepository.save(journey);
    }

    /**
     * Updates an existing journey for the given user.
     * <p>
     * The method verifies that the journey exists and that it is owned by the user;
     * then it updates the journey's details.
     * </p>
     *
     * @param id             the ID of the journey to update
     * @param journeyDetails the updated journey data
     * @param user           the user attempting to update the journey
     * @return the updated journey
     * @throws RuntimeException if the journey does not belong to the user or is not found
     */
    public Journey updateJourney(Long id, Journey journeyDetails, User user) {
        Journey journey = getJourneyById(id);

        // Verify that the journey belongs to the user.
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
     * Deletes a journey by its ID.
     *
     * @param id   the ID of the journey to delete
     * @param user the user attempting to delete the journey
     * @throws RuntimeException if the journey does not belong to the user or is not found
     */
    public void deleteJourney(Long id, User user) {
        Journey journey = getJourneyById(id);

        // Verify that the journey belongs to the user.
        if (!journey.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this journey.");
        }

        journeyRepository.deleteById(id);
    }
}