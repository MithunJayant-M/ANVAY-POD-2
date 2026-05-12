package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByInstitutionId(Long institutionId);
    long countByInstitutionId(Long institutionId);

    @Query("SELECT SUM(c.memberCount) FROM Club c WHERE c.institutionId = :institutionId")
    Long sumMemberCountByInstitutionId(@Param("institutionId") Long institutionId);

    // Clubs that the given user has joined (status APPROVED). Single SQL with
    // a sub-select avoids N+1 lookups when building the student dashboard.
    @Query("SELECT c FROM Club c WHERE c.clubId IN " +
           "(SELECT cm.clubId FROM ClubMember cm WHERE cm.userId = :userId AND cm.status = 'APPROVED')")
    List<Club> findApprovedClubsByUserId(@Param("userId") Long userId);
}
