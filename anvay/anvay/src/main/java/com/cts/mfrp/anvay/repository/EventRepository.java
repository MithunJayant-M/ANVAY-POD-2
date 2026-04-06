package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByInstitutionId(Integer institutionId);
    long countByInstitutionId(Integer institutionId);

    @Query("SELECT MONTH(e.createdAt), COUNT(e) FROM Event e GROUP BY MONTH(e.createdAt) ORDER BY MONTH(e.createdAt)")
    List<Object[]> countEventsByMonth();

    @Query("SELECT e.institutionId, COUNT(e) FROM Event e GROUP BY e.institutionId")
    List<Object[]> countEventsByInstitution();
}
