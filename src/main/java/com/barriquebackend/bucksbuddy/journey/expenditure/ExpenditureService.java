package com.barriquebackend.bucksbuddy.journey.expenditure;

import com.barriquebackend.bucksbuddy.journey.Journey;
import com.barriquebackend.bucksbuddy.journey.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final JourneyRepository journeyRepository;

    @Autowired
    public ExpenditureService(ExpenditureRepository expenditureRepository, JourneyRepository journeyRepository) {
        this.expenditureRepository = expenditureRepository;
        this.journeyRepository = journeyRepository;
    }

    /**
     * Retrieve all expenditures for a specific journey and user.
     */
    public List<Expenditure> getAllExpendituresByJourneyId(Long journeyId, Long userId) {
        return expenditureRepository.findAllByJourney_JourneyIdAndJourney_User_Id(journeyId, userId);
    }

    /**
     * Retrieve an expenditure by its ID.
     */
    public Optional<Expenditure> getExpenditureById(Long expenditureId) {
        return expenditureRepository.findByExpenditureId(expenditureId);
    }

    /**
     * Create a new expenditure for a given journey.
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
     * Update an existing expenditure.
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
     * Delete an expenditure by its ID.
     */
    public boolean deleteExpenditure(Long expenditureId) {
        if (expenditureRepository.existsById(expenditureId)) {
            expenditureRepository.deleteById(expenditureId);
            return true;
        }
        return false;
    }
}