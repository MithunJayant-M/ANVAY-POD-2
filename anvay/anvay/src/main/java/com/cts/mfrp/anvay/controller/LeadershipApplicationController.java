package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.entity.LeadershipApplication;
import com.cts.mfrp.anvay.repository.LeadershipApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leadership-applications")
@RequiredArgsConstructor
public class LeadershipApplicationController {

    private final LeadershipApplicationRepository applicationRepository;

    @PostMapping
    public ResponseEntity<LeadershipApplication> apply(@RequestBody LeadershipApplication application) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationRepository.save(application));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<LeadershipApplication>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(applicationRepository.findByClubId(clubId));
    }

    @GetMapping("/club/{clubId}/pending")
    public ResponseEntity<List<LeadershipApplication>> getPendingByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(applicationRepository.findByClubIdAndStatus(clubId, "pending"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeadershipApplication>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationRepository.findByUserId(userId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return applicationRepository.findById(id).map(app -> {
            app.setStatus("approved");
            return ResponseEntity.ok(applicationRepository.save(app));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        return applicationRepository.findById(id).map(app -> {
            app.setStatus("rejected");
            return ResponseEntity.ok(applicationRepository.save(app));
        }).orElse(ResponseEntity.notFound().build());
    }
}
