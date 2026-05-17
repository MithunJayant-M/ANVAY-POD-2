package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    // LEFT JOIN FETCH (not INNER) so members are returned even when the
    // referenced user row is missing — otherwise orphan refs silently
    // filter the entire result set, making the table look empty.
    @Query("SELECT cm FROM ClubMember cm LEFT JOIN FETCH cm.user WHERE cm.clubId = :clubId")
    List<ClubMember> findByClubId(@Param("clubId") Long clubId);

    @Query("SELECT cm FROM ClubMember cm LEFT JOIN FETCH cm.user WHERE cm.clubId = :clubId AND cm.status = :status")
    List<ClubMember> findByClubIdAndStatus(@Param("clubId") Long clubId, @Param("status") String status);
    List<ClubMember> findByUserId(Long userId);

    // JOIN FETCH variant for the DTO mapping path — pulls the user in the
    // same query so toSummary() can read u.getFirstName() etc. without an
    // N+1 storm and without LazyInitializationException after the tx closes.
    @Query("SELECT cm FROM ClubMember cm LEFT JOIN FETCH cm.user WHERE cm.userId = :userId")
    List<ClubMember> findMembershipsByUserWithUser(@Param("userId") Long userId);
    Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId);
    long countByClubId(Long clubId);
    long countByClubIdAndStatus(Long clubId, String status);
    boolean existsByClubIdAndUserId(Long clubId, Long userId);
}
