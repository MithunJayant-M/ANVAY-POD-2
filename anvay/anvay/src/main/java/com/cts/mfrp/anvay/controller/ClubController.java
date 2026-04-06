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

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
@Slf4j
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ClubDashboardDTO>> getAllClubsByInstitution(@PathVariable Integer institutionId) {
        try {
            List<ClubDashboardDTO> clubs = clubService.getAllClubsByInstitution(institutionId);
            return ResponseEntity.ok(clubs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{clubId}")
    public ResponseEntity<Club> updateClub(@PathVariable Integer clubId, @RequestBody Club updatedClub) {
        try {
            Club club = clubService.updateClub(clubId, updatedClub);
            return ResponseEntity.ok(club);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<ClubMember>> getClubMembers(@PathVariable Integer clubId) {
        try {
            List<ClubMember> members = clubService.getClubMembers(clubId);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{clubId}/members/approved")
    public ResponseEntity<List<ClubMember>> getApprovedMembers(@PathVariable Integer clubId) {
        try {
            List<ClubMember> members = clubService.getApprovedMembers(clubId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<Club> getClubById(@PathVariable Integer clubId) {
        try {
            Club club = clubService.getClubById(clubId);
            return ResponseEntity.ok(club);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        try {
            Club createdClub = clubService.createClub(club);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClub);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Integer clubId) {
        try {
            clubService.deleteClub(clubId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
