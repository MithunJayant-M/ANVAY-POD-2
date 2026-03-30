package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.Institution;

public interface InstitutionService {
    Institution createInstitution(Institution institution);
    Institution getInstitutionById(Long institutionId);
    List<Institution> getAllInstitutions();
    Institution updateInstitution(Long institutionId, Institution institution);
    void deleteInstitution(Long institutionId);
}
