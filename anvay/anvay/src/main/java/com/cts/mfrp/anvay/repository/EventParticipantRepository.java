package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Integer> {
    List<EventParticipant> findByEventId(Integer eventId);
    List<EventParticipant> findByUserId(Integer userId);
    long countByEventId(Integer eventId);

    @Query("SELECT ep.userId, SUM(ep.pointsEarned) FROM EventParticipant ep GROUP BY ep.userId ORDER BY SUM(ep.pointsEarned) DESC")
    List<Object[]> findUserPointsRanking();

    @Query("SELECT SUM(ep.pointsEarned) FROM EventParticipant ep " +
           "JOIN Event e ON ep.eventId = e.eventId WHERE e.institutionId = :institutionId")
    Long sumPointsByInstitutionId(Integer institutionId);
}
