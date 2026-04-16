package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    List<Achievement> findByUserId(Integer userId);
    List<Achievement> findByClubId(Integer clubId);

    @Query("SELECT SUM(a.points) FROM Achievement a WHERE a.userId = :userId")
    Integer sumPointsByUserId(Integer userId);
}
