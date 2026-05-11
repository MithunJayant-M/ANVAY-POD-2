package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.dto.StudentDashboardDTO;
import com.cts.mfrp.anvay.dto.StudentSummaryDTO;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<StudentDashboardDTO> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getFullDashboard(id));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/institution/{institutionId}/leaderboard")
    public ResponseEntity<List<User>> getInstitutionLeaderboard(@PathVariable Long institutionId) {
        return ResponseEntity.ok(userRepository.findStudentsByInstitutionId(institutionId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<User>> getGlobalLeaderboard() {
        return ResponseEntity.ok(userRepository.findAllStudents());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /**
     * Paginated, lean projection — preferred for list views.
     * Skips profilePicture (LONGTEXT) to avoid OOM on large institutions.
     * Query params: ?page=0&size=20&sort=totalPoints,desc
     */
    @GetMapping("/page")
    public ResponseEntity<Page<StudentSummaryDTO>> getStudentsPage(
            @PageableDefault(size = 20, sort = "totalPoints", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userRepository.findStudentSummaries(pageable));
    }

    @GetMapping("/institution/{institutionId}/page")
    public ResponseEntity<Page<StudentSummaryDTO>> getStudentsByInstitutionPage(
            @PathVariable Long institutionId,
            @PageableDefault(size = 20, sort = "totalPoints", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userRepository.findStudentSummariesByInstitution(institutionId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        user.setInstitutionId(null);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}