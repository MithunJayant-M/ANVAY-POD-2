package com.cts.mfrp.anvay.service;

import com.cts.mfrp.anvay.dto.LoginResponse;
import com.cts.mfrp.anvay.dto.RegisterInstitutionRequest;

public interface InstitutionService {
    LoginResponse registerInstitution(RegisterInstitutionRequest request);
}
