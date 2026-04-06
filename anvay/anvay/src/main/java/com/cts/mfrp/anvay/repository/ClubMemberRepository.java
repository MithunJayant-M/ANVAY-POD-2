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
    long countByClubId(Integer clubId);
    boolean existsByClubIdAndUserId(Integer clubId, Integer userId);
}
