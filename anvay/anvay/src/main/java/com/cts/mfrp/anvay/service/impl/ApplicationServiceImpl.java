package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.entity.LeadershipApplication;
import com.cts.mfrp.anvay.repository.LeadershipApplicationRepository;
import com.cts.mfrp.anvay.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final LeadershipApplicationRepository leadershipApplicationRepository;

    @Override
    public LeadershipApplication createApplication(LeadershipApplication application) {
        return leadershipApplicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadershipApplication getApplicationById(Integer applicationId) {
        return leadershipApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadershipApplication> getApplicationsByClubId(Integer clubId) {
        return leadershipApplicationRepository.findByClubId(clubId);
    }

    @Override
    public LeadershipApplication updateApplication(Integer applicationId, LeadershipApplication application) {
        LeadershipApplication existing = getApplicationById(applicationId);
        if (application.getStatus() != null) existing.setStatus(application.getStatus());
        if (application.getExperience() != null) existing.setExperience(application.getExperience());
        return leadershipApplicationRepository.save(existing);
    }

    @Override
    public void deleteApplication(Integer applicationId) {
        leadershipApplicationRepository.deleteById(applicationId);
    }
}
