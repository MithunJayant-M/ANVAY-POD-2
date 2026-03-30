package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ClubMember entity.
 * Provides database operations for club membership management.
 */
@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    /**
     * Find all members of a specific club.
     * @param clubId the club ID
     * @return list of club members
     */
    List<ClubMember> findByClubId(Long clubId);

    /**
     * Find all club members that are approved.
     * @param clubId the club ID
     * @return list of approved club members
     */
    List<ClubMember> findByClubIdAndStatus(Long clubId, String status);

    /**
     * Count the total number of members in a club.
     * @param clubId the club ID
     * @return the count of members
     */
    Long countByClubId(Long clubId);

    /**
     * Count the pending join requests for a club.
     * @param clubId the club ID
     * @return the count of pending requests
     */
    Long countByClubIdAndStatus(Long clubId, String status);
}
