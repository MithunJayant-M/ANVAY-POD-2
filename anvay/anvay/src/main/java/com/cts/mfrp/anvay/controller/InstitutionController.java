package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.entity.Institution;
import com.cts.mfrp.anvay.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Institution>> getActiveInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions()
                .stream()
                .filter(i -> "active".equals(i.getStatus()))
                .toList());
    }

    @GetMapping("/{institutionId}")
    public ResponseEntity<Institution> getInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(institutionService.getInstitutionById(institutionId));
    }

    @PutMapping("/{institutionId}")
    public ResponseEntity<Institution> updateInstitution(@PathVariable Long institutionId, @RequestBody Institution institution) {
        return ResponseEntity.ok(institutionService.updateInstitution(institutionId, institution));
    }

    @DeleteMapping("/{institutionId}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Long institutionId) {
        institutionService.deleteInstitution(institutionId);
        return ResponseEntity.noContent().build();
    }
}
