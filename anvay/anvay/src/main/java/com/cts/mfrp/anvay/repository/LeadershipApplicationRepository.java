package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.LeadershipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadershipApplicationRepository extends JpaRepository<LeadershipApplication, Integer> {
    List<LeadershipApplication> findByClubId(Integer clubId);
    List<LeadershipApplication> findByUserId(Integer userId);
    List<LeadershipApplication> findByStatus(String status);
    List<LeadershipApplication> findByClubIdAndStatus(Integer clubId, String status);
    long countByClubId(Integer clubId);
}
