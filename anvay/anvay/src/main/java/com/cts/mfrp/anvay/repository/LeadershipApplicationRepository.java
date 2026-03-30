package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.LeadershipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for LeadershipApplication entity.
 * Provides database operations for leadership application management.
 */
@Repository
public interface LeadershipApplicationRepository extends JpaRepository<LeadershipApplication, Long> {

    /**
     * Find all leadership applications for a specific club.
     * @param clubId the club ID
     * @return list of leadership applications
     */
    List<LeadershipApplication> findByClubId(Long clubId);

    /**
     * Find applications by club ID and status.
     * @param clubId the club ID
     * @param status the application status
     * @return list of applications with the specified status
     */
    List<LeadershipApplication> findByClubIdAndStatus(Long clubId, String status);

    /**
     * Count all leadership applications for a club.
     * @param clubId the club ID
     * @return the total count of applications
     */
    Long countByClubId(Long clubId);

    /**
     * Count leadership applications by club and status.
     * @param clubId the club ID
     * @param status the application status
     * @return the count of applications with the specified status
     */
    Long countByClubIdAndStatus(Long clubId, String status);
}
