package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.dto.EventSummaryDTO;
import com.cts.mfrp.anvay.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByClubId(Long clubId);
    List<Event> findByStatusNot(String status);

    @Query("SELECT e, (CASE WHEN p.id IS NOT NULL THEN true ELSE false END) " +
           "FROM Event e LEFT JOIN EventParticipant p ON e.eventId = p.eventId AND p.userId = :userId")
    List<Object[]> findAllEventsWithRegistrationStatus(@Param("userId") Long userId);

    @Query("SELECT e, (CASE WHEN p.id IS NOT NULL THEN true ELSE false END) " +
           "FROM Event e JOIN Club c ON e.clubId = c.clubId " +
           "LEFT JOIN EventParticipant p ON e.eventId = p.eventId AND p.userId = :userId " +
           "WHERE c.institutionId = :institutionId OR e.participantType = 'all'")
    List<Object[]> findEventsForStudent(@Param("userId") Long userId, @Param("institutionId") Long institutionId);

    @Query("SELECT COUNT(e) FROM Event e JOIN Club c ON e.clubId = c.clubId WHERE c.institutionId = :institutionId")
    long countByInstitutionId(@Param("institutionId") Long institutionId);

    @Query("SELECT e FROM Event e JOIN Club c ON e.clubId = c.clubId WHERE c.institutionId = :institutionId")
    List<Event> findByInstitutionId(@Param("institutionId") Long institutionId);

    @Query("SELECT e FROM Event e JOIN FETCH e.club cl JOIN FETCH cl.institution WHERE e.winnersStatus = :status")
    List<Event> findByWinnersStatus(@Param("status") String status);

    @Query("SELECT MONTH(e.startDate), COUNT(e) FROM Event e WHERE e.startDate IS NOT NULL GROUP BY MONTH(e.startDate) ORDER BY MONTH(e.startDate)")
    List<Object[]> countEventsByMonth();

    // Institution event list — DTO projection avoids loading imageData LONGTEXT
    @Query("SELECT new com.cts.mfrp.anvay.dto.EventSummaryDTO(" +
           "  e.eventId, e.clubId, e.eventName, e.category, e.location, " +
           "  e.startDate, e.endDate, e.status, e.maxParticipants, " +
           "  e.registrationFee, e.participantType, e.registrationDeadline) " +
           "FROM Event e JOIN Club c ON e.clubId = c.clubId " +
           "WHERE c.institutionId = :institutionId " +
           "ORDER BY e.startDate DESC")
    List<com.cts.mfrp.anvay.dto.EventSummaryDTO> findEventSummariesByInstitutionList(@Param("institutionId") Long institutionId);

    // Global event list — same projection. Ordered by startDate DESC so the
    // super-admin "all events" view shows recent activity first.
    @Query("SELECT new com.cts.mfrp.anvay.dto.EventSummaryDTO(" +
           "  e.eventId, e.clubId, e.eventName, e.category, e.location, " +
           "  e.startDate, e.endDate, e.status, e.maxParticipants, " +
           "  e.registrationFee, e.participantType, e.registrationDeadline) " +
           "FROM Event e ORDER BY e.startDate DESC")
    List<com.cts.mfrp.anvay.dto.EventSummaryDTO> findAllEventSummaries();

    // ── Paginated summary projections (new, non-breaking) ──────────────────
    // JPQL constructor projection: selects scalar columns directly into a DTO.
    // Avoids loading the Event entity, skips lazy `club` resolution, and never
    // touches the LONGTEXT `imageData` column → no N+1, much smaller payload.

    @Query(
        value = "SELECT new com.cts.mfrp.anvay.dto.EventSummaryDTO(" +
                "  e.eventId, e.clubId, e.eventName, e.category, e.location, " +
                "  e.startDate, e.endDate, e.status, e.maxParticipants, " +
                "  e.registrationFee, e.participantType, e.registrationDeadline) " +
                "FROM Event e",
        countQuery = "SELECT COUNT(e) FROM Event e"
    )
    Page<EventSummaryDTO> findAllSummaries(Pageable pageable);

    @Query(
        value = "SELECT new com.cts.mfrp.anvay.dto.EventSummaryDTO(" +
                "  e.eventId, e.clubId, e.eventName, e.category, e.location, " +
                "  e.startDate, e.endDate, e.status, e.maxParticipants, " +
                "  e.registrationFee, e.participantType, e.registrationDeadline) " +
                "FROM Event e JOIN Club c ON e.clubId = c.clubId " +
                "WHERE c.institutionId = :institutionId",
        countQuery = "SELECT COUNT(e) FROM Event e JOIN Club c ON e.clubId = c.clubId " +
                     "WHERE c.institutionId = :institutionId"
    )
    Page<EventSummaryDTO> findSummariesByInstitution(@Param("institutionId") Long institutionId,
                                                    Pageable pageable);
}
