package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.LeadershipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadershipApplicationRepository extends JpaRepository<LeadershipApplication, Long> {
    List<LeadershipApplication> findByClubId(Long clubId);
    List<LeadershipApplication> findByUserId(Long userId);
    List<LeadershipApplication> findByStatus(String status);
    List<LeadershipApplication> findByClubIdAndStatus(Long clubId, String status);
    long countByClubId(Long clubId);
}
