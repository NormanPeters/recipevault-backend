package com.barriquebackend.bucksbuddy.journey.expenditure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    Optional<Expenditure> findByExpenditureId(Long id);

    // Query expenditures by the journey's id and the journey's user id.
    List<Expenditure> findAllByJourney_JourneyIdAndJourney_User_Id(Long journeyId, Long userId);
}