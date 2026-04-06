package com.cts.mfrp.anvay.service;

import com.cts.mfrp.anvay.dto.ClubDashboardDTO;
import com.cts.mfrp.anvay.entity.Club;
import com.cts.mfrp.anvay.entity.ClubMember;

import java.util.List;

public interface ClubService {
    List<ClubDashboardDTO> getAllClubsByInstitution(Integer institutionId);
    Club updateClub(Integer clubId, Club updatedClub);
    List<ClubMember> getClubMembers(Integer clubId);
    List<ClubMember> getApprovedMembers(Integer clubId);
    Club getClubById(Integer clubId);
    Club createClub(Club club);
    void deleteClub(Integer clubId);
}
