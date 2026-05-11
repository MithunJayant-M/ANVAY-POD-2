package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.dto.StudentSummaryDTO;
import com.cts.mfrp.anvay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByInstitutionId(Long institutionId);
    List<User> findByInstitutionIdAndRole(Long institutionId, String role);
    long countByInstitutionId(Long institutionId);
    long countByInstitutionIdAndRole(Long institutionId, String role);
    List<User> findByRole(String role);
    long countByRole(String role);
    List<User> findByRoleOrderByTotalPointsDesc(String role);

    @Query("SELECT u FROM User u WHERE u.institutionId = :institutionId AND u.role IN ('student', 'club_leader') ORDER BY u.totalPoints DESC NULLS LAST")
    List<User> findStudentsByInstitutionId(@Param("institutionId") Long institutionId);

    @Query("SELECT u FROM User u WHERE u.role IN ('student', 'club_leader') ORDER BY u.totalPoints DESC NULLS LAST")
    List<User> findAllStudents();

    @Query("SELECT COUNT(u) FROM User u WHERE u.institutionId = :institutionId AND u.role IN ('student', 'club_leader')")
    long countStudentsByInstitutionId(@Param("institutionId") Long institutionId);

    // Leaderboard view — DTO projection avoids loading profilePicture LONGTEXT
    @Query("SELECT new com.cts.mfrp.anvay.dto.StudentSummaryDTO(" +
           "  u.userId, u.institutionId, u.email, u.firstName, u.lastName, u.role, " +
           "  u.totalPoints, u.rankInLeaderboard, u.registeredEventsCount, " +
           "  u.joinedClubsCount, u.leadingClubId, u.studentIdNumber) " +
           "FROM User u WHERE u.institutionId = :institutionId " +
           "  AND u.role IN ('student', 'club_leader') " +
           "ORDER BY u.totalPoints DESC NULLS LAST")
    List<com.cts.mfrp.anvay.dto.StudentSummaryDTO> findStudentSummaryLeaderboard(@Param("institutionId") Long institutionId);

    @Query("SELECT new com.cts.mfrp.anvay.dto.StudentSummaryDTO(" +
           "  u.userId, u.institutionId, u.email, u.firstName, u.lastName, u.role, " +
           "  u.totalPoints, u.rankInLeaderboard, u.registeredEventsCount, " +
           "  u.joinedClubsCount, u.leadingClubId, u.studentIdNumber) " +
           "FROM User u WHERE u.role IN ('student', 'club_leader') " +
           "ORDER BY u.totalPoints DESC NULLS LAST")
    List<com.cts.mfrp.anvay.dto.StudentSummaryDTO> findAllStudentSummaries();

    // ── Paginated summary projections (new, non-breaking) ──────────────────
    // Skips `profilePicture` (LONGTEXT) and `password`. The User entity's
    // `institution` association is EAGER-fetched normally; constructor
    // projection bypasses that, avoiding a per-row institution lookup (N+1).

    @Query(
        value = "SELECT new com.cts.mfrp.anvay.dto.StudentSummaryDTO(" +
                "  u.userId, u.institutionId, u.email, u.firstName, u.lastName, u.role, " +
                "  u.totalPoints, u.rankInLeaderboard, u.registeredEventsCount, " +
                "  u.joinedClubsCount, u.leadingClubId, u.studentIdNumber) " +
                "FROM User u WHERE u.role IN ('student', 'club_leader')",
        countQuery = "SELECT COUNT(u) FROM User u WHERE u.role IN ('student', 'club_leader')"
    )
    Page<StudentSummaryDTO> findStudentSummaries(Pageable pageable);

    @Query(
        value = "SELECT new com.cts.mfrp.anvay.dto.StudentSummaryDTO(" +
                "  u.userId, u.institutionId, u.email, u.firstName, u.lastName, u.role, " +
                "  u.totalPoints, u.rankInLeaderboard, u.registeredEventsCount, " +
                "  u.joinedClubsCount, u.leadingClubId, u.studentIdNumber) " +
                "FROM User u WHERE u.institutionId = :institutionId " +
                "  AND u.role IN ('student', 'club_leader')",
        countQuery = "SELECT COUNT(u) FROM User u WHERE u.institutionId = :institutionId " +
                     "  AND u.role IN ('student', 'club_leader')"
    )
    Page<StudentSummaryDTO> findStudentSummariesByInstitution(@Param("institutionId") Long institutionId,
                                                              Pageable pageable);
}
