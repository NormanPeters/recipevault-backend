package com.barriquebackend.bucksbuddy.journey.expenditure;

import com.barriquebackend.bucksbuddy.journey.Journey;
import com.barriquebackend.bucksbuddy.journey.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to expenditures.
 * Provides methods for creating, retrieving, updating, and deleting expenditures
 * that are associated with a specific journey and user.
 */
@Service
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final JourneyRepository journeyRepository;

    /**
     * Constructs an ExpenditureService with the given repositories.
     *
     * @param expenditureRepository the repository for performing CRUD operations on expenditures
     * @param journeyRepository     the repository for retrieving journeys
     */
    @Autowired
    public ExpenditureService(ExpenditureRepository expenditureRepository, JourneyRepository journeyRepository) {
        this.expenditureRepository = expenditureRepository;
        this.journeyRepository = journeyRepository;
    }

    /**
     * Retrieves all expenditures for a specific journey that belong to a given user.
     *
     * @param journeyId the ID of the journey
     * @param userId    the ID of the user
     * @return a list of expenditures associated with the journey and user
     */
    public List<Expenditure> getAllExpendituresByJourneyId(Long journeyId, Long userId) {
        return expenditureRepository.findAllByJourney_JourneyIdAndJourney_User_Id(journeyId, userId);
    }

    /**
     * Retrieves an expenditure by its ID.
     *
     * @param expenditureId the ID of the expenditure
     * @return an Optional containing the expenditure if found, or empty otherwise
     */
    public Optional<Expenditure> getExpenditureById(Long expenditureId) {
        return expenditureRepository.findByExpenditureId(expenditureId);
    }

    /**
     * Creates a new expenditure for a given journey.
     *
     * @param journeyId   the ID of the journey
     * @param expenditure the expenditure to be created
     * @return the created expenditure
     * @throws IllegalArgumentException if the journey is not found
     */
    public Expenditure createExpenditure(Long journeyId, Expenditure expenditure) {
        Optional<Journey> journeyOpt = journeyRepository.findById(journeyId);
        if (journeyOpt.isPresent()) {
            Journey journey = journeyOpt.get();
            expenditure.setJourney(journey);
            return expenditureRepository.save(expenditure);
        } else {
            throw new IllegalArgumentException("Journey not found for id: " + journeyId);
        }
    }

    /**
     * Updates an existing expenditure.
     *
     * @param expenditureId     the ID of the expenditure to update
     * @param updatedExpenditure the updated expenditure data
     * @return an Optional containing the updated expenditure if the update was successful, or empty otherwise
     */
    public Optional<Expenditure> updateExpenditure(Long expenditureId, Expenditure updatedExpenditure) {
        return expenditureRepository.findByExpenditureId(expenditureId).map(expenditure -> {
            expenditure.setName(updatedExpenditure.getName());
            expenditure.setAmount(updatedExpenditure.getAmount());
            expenditure.setDate(updatedExpenditure.getDate());
            return expenditureRepository.save(expenditure);
        });
    }

    /**
     * Deletes an expenditure by its ID.
     *
     * @param expenditureId the ID of the expenditure to delete
     * @return true if the expenditure was deleted successfully, false otherwise
     */
    public boolean deleteExpenditure(Long expenditureId) {
        if (expenditureRepository.existsById(expenditureId)) {
            expenditureRepository.deleteById(expenditureId);
            return true;
        }
        return false;
    }
}