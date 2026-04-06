package com.cts.mfrp.anvay.service;

import com.cts.mfrp.anvay.dto.AnalyticsDto;
import com.cts.mfrp.anvay.dto.DashboardStatsDto;
import com.cts.mfrp.anvay.dto.InstitutionDto;

import java.util.List;

public interface SuperAdminService {
    DashboardStatsDto getDashboardStats();
    List<InstitutionDto> getAllInstitutions(String search);
    InstitutionDto getInstitutionById(Integer institutionId);
    InstitutionDto approveInstitution(Integer institutionId);
    InstitutionDto deactivateInstitution(Integer institutionId);
    AnalyticsDto getAnalytics();
}
