package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Club entity.
 * Provides database operations for clubs including custom queries
 * for dashboard data retrieval.
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    /**
     * Find all clubs for a specific institution.
     * @param institutionId the institution ID
     * @return list of clubs belonging to the institution
     */
    List<Club> findByInstitutionId(Long institutionId);

    /**
     * Find a club by ID and institution ID.
     * @param clubId the club ID
     * @param institutionId the institution ID
     * @return optional containing the club if found
     */
    Optional<Club> findByClubIdAndInstitutionId(Long clubId, Long institutionId);

    /**
     * Count the number of club members for a specific club.
     * @param clubId the club ID
     * @return the count of approved members
     */
    @Query("SELECT COUNT(cm) FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.status = 'APPROVED'")
    Long countApprovedMembers(@Param("clubId") Long clubId);

    /**
     * Count the number of pending join requests for a club.
     * @param clubId the club ID
     * @return the count of pending join requests
     */
    @Query("SELECT COUNT(cm) FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.status = 'PENDING'")
    Long countPendingJoinRequests(@Param("clubId") Long clubId);

    /**
     * Count the number of pending leadership applications for a club.
     * @param clubId the club ID
     * @return the count of pending leadership applications
     */
    @Query("SELECT COUNT(la) FROM LeadershipApplication la WHERE la.clubId = :clubId AND la.status = 'PENDING'")
    Long countPendingLeadershipApps(@Param("clubId") Long clubId);

    /**
     * Get all leadership applications count for a club (including all statuses).
     * @param clubId the club ID
     * @return the total count of leadership applications
     */
    @Query("SELECT COUNT(la) FROM LeadershipApplication la WHERE la.clubId = :clubId")
    Long countAllLeadershipApps(@Param("clubId") Long clubId);
}
