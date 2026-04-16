package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.LeadershipApplication;

public interface ApplicationService {
    LeadershipApplication createApplication(LeadershipApplication application);
    LeadershipApplication getApplicationById(Integer applicationId);
    List<LeadershipApplication> getApplicationsByClubId(Integer clubId);
    LeadershipApplication updateApplication(Integer applicationId, LeadershipApplication application);
    void deleteApplication(Integer applicationId);
}
