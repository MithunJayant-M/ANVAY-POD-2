package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.LeadershipApplication;

public interface ApplicationService {
    LeadershipApplication createApplication(LeadershipApplication application);
    LeadershipApplication getApplicationById(Long applicationId);
    List<LeadershipApplication> getApplicationsByClubId(Long clubId);
    LeadershipApplication updateApplication(Long applicationId, LeadershipApplication application);
    void deleteApplication(Long applicationId);
}
