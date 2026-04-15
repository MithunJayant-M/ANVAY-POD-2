package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByClubId(Long clubId);

    @Query("SELECT e, " +
            "(CASE WHEN p.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Event e " +
            "LEFT JOIN EventParticipant p ON e.eventId = p.eventId AND p.userId = :userId")
    List<Object[]> findAllEventsWithRegistrationStatus(@Param("userId") Long userId);
}