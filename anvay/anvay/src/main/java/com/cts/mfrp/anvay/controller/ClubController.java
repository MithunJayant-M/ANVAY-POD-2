package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.dto.ClubDashboardDTO;
import com.cts.mfrp.anvay.entity.Club;
import com.cts.mfrp.anvay.entity.ClubMember;
import com.cts.mfrp.anvay.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Club-related endpoints.
 * Handles HTTP requests for club management operations including dashboard views,
 * club updates, and member retrieval.
 *
 * Base path: /api/clubs
 */
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
@Slf4j
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/all")
    public ResponseEntity<List<Club>> getAllClubs(){
        return ResponseEntity.ok(clubService.getClubs());
    }

    /**
     * Get all clubs for an institution.
     * Returns dashboard data with member counts, join requests, and leadership applications.
     *
     * @param institutionId the institution ID
     * @return ResponseEntity containing list of ClubDashboardDTO objects
     * @apiNote Used for "Your Clubs" data table UI (US16P2_12)
     */
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ClubDashboardDTO>> getAllClubsByInstitution(
            @PathVariable Long institutionId) {
        log.info("Received request to fetch clubs for institution: {}", institutionId);

        try {
            List<ClubDashboardDTO> clubs = clubService.getAllClubsByInstitution(institutionId);
            log.info("Successfully fetched {} clubs for institution: {}", clubs.size(), institutionId);
            return ResponseEntity.ok(clubs);
        } catch (IllegalArgumentException e) {
            log.error("Invalid institution ID: {}", institutionId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error fetching clubs for institution: {}", institutionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a club's details.
     * Allows modification of club name and category.
     *
     * @param clubId the club ID
     * @param updatedClub the club entity with updated values
     * @return ResponseEntity containing the updated Club entity
     * @apiNote Used for Club Action Management - Edit club (US16P2_13)
     */
    @PutMapping("/{clubId}")
    public ResponseEntity<Club> updateClub(
            @PathVariable Long clubId,
            @RequestBody Club updatedClub) {
        log.info("Received request to update club: {}", clubId);

        try {
            Club club = clubService.updateClub(clubId, updatedClub);
            log.info("Club updated successfully: {}", clubId);
            return ResponseEntity.ok(club);
        } catch (IllegalArgumentException e) {
            log.error("Club not found or invalid data for ID: {}", clubId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating club: {}", clubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all members of a specific club.
     * Returns a list of all club members regardless of membership status.
     *
     * @param clubId the club ID
     * @return ResponseEntity containing list of ClubMember entities
     * @apiNote Used for Club Action Management - View members (US16P2_13)
     */
    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<ClubMember>> getClubMembers(
            @PathVariable Long clubId) {
        log.info("Received request to fetch members for club: {}", clubId);

        try {
            List<ClubMember> members = clubService.getClubMembers(clubId);
            log.info("Successfully fetched {} members for club: {}", members.size(), clubId);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            log.error("Club not found with ID: {}", clubId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching members for club: {}", clubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get approved members of a specific club.
     *
     * @param clubId the club ID
     * @return ResponseEntity containing list of approved ClubMember entities
     */
    @GetMapping("/{clubId}/members/approved")
    public ResponseEntity<List<ClubMember>> getApprovedMembers(
            @PathVariable Long clubId) {
        log.info("Received request to fetch approved members for club: {}", clubId);

        try {
            List<ClubMember> members = clubService.getApprovedMembers(clubId);
            log.info("Successfully fetched {} approved members for club: {}", members.size(), clubId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            log.error("Error fetching approved members for club: {}", clubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a specific club by ID.
     *
     * @param clubId the club ID
     * @return ResponseEntity containing the Club entity
     */
    @GetMapping("/{clubId}")
    public ResponseEntity<Club> getClubById(@PathVariable Long clubId) {
        log.info("Received request to fetch club: {}", clubId);

        try {
            Club club = clubService.getClubById(clubId);
            log.info("Successfully fetched club: {}", clubId);
            return ResponseEntity.ok(club);
        } catch (IllegalArgumentException e) {
            log.error("Club not found with ID: {}", clubId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching club: {}", clubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new club.
     *
     * @param club the club entity to create
     * @return ResponseEntity containing the created Club entity
     */
    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        log.info("Received request to create new club with name: {}", club.getClubName());

        try {
            Club createdClub = clubService.createClub(club);
            log.info("Club created successfully with ID: {}", createdClub.getClubId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClub);
        } catch (Exception e) {
            log.error("Error creating club", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a club by ID.
     *
     * @param clubId the club ID
     * @return ResponseEntity with no content on success
     */
    @DeleteMapping("/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId) {
        log.info("Received request to delete club: {}", clubId);

        try {
            clubService.deleteClub(clubId);
            log.info("Club deleted successfully: {}", clubId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Club not found with ID: {}", clubId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting club: {}", clubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
