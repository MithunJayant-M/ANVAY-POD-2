package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {
    List<Club> findByInstitutionId(Integer institutionId);
    long countByInstitutionId(Integer institutionId);

    @Query("SELECT SUM(c.memberCount) FROM Club c WHERE c.institutionId = :institutionId")
    Long sumMemberCountByInstitutionId(Integer institutionId);
}
