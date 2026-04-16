package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Integer> {
    List<ClubMember> findByClubId(Integer clubId);
    List<ClubMember> findByUserId(Integer userId);
    Optional<ClubMember> findByClubIdAndUserId(Integer clubId, Integer userId);
    List<ClubMember> findByClubIdAndStatus(Integer clubId, String status);
    long countByClubId(Integer clubId);
    long countByClubIdAndStatus(Integer clubId, String status);
    boolean existsByClubIdAndUserId(Integer clubId, Integer userId);
}
