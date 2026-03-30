package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.entity.Institution;
import com.cts.mfrp.anvay.repository.InstitutionRepository;
import com.cts.mfrp.anvay.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Override
    public Institution createInstitution(Institution institution) {
        institution.setCreatedAt(LocalDateTime.now());
        institution.setUpdatedAt(LocalDateTime.now());
        return institutionRepository.save(institution);
    }

    @Override
    @Transactional(readOnly = true)
    public Institution getInstitutionById(Long institutionId) {
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }

    @Override
    public Institution updateInstitution(Long institutionId, Institution institution) {
        Institution existing = getInstitutionById(institutionId);
        if (institution.getName() != null) existing.setName(institution.getName());
        if (institution.getLocation() != null) existing.setLocation(institution.getLocation());
        existing.setUpdatedAt(LocalDateTime.now());
        return institutionRepository.save(existing);
    }

    @Override
    public void deleteInstitution(Long institutionId) {
        institutionRepository.deleteById(institutionId);
    }
}
