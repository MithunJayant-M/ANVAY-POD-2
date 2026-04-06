package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.dto.ClubDashboardDTO;
import com.cts.mfrp.anvay.entity.Club;
import com.cts.mfrp.anvay.entity.ClubMember;
import com.cts.mfrp.anvay.repository.ClubRepository;
import com.cts.mfrp.anvay.repository.ClubMemberRepository;
import com.cts.mfrp.anvay.repository.LeadershipApplicationRepository;
import com.cts.mfrp.anvay.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final LeadershipApplicationRepository leadershipApplicationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClubDashboardDTO> getAllClubsByInstitution(Integer institutionId) {
        log.info("Fetching all clubs for institution ID: {}", institutionId);
        List<Club> clubs = clubRepository.findByInstitutionId(institutionId);
        return clubs.stream()
                .map(this::buildClubDashboardDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Club updateClub(Integer clubId, Club updatedClub) {
        log.info("Updating club with ID: {}", clubId);
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with ID: " + clubId));

        if (updatedClub.getClubName() != null && !updatedClub.getClubName().isBlank()) {
            club.setClubName(updatedClub.getClubName());
        }
        if (updatedClub.getCategory() != null && !updatedClub.getCategory().isBlank()) {
            club.setCategory(updatedClub.getCategory());
        }
        if (updatedClub.getMemberCount() != null) {
            club.setMemberCount(updatedClub.getMemberCount());
        }

        return clubRepository.save(club);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMember> getClubMembers(Integer clubId) {
        log.info("Fetching all members for club ID: {}", clubId);
        clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with ID: " + clubId));
        return clubMemberRepository.findByClubId(clubId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMember> getApprovedMembers(Integer clubId) {
        log.info("Fetching approved members for club ID: {}", clubId);
        return clubMemberRepository.findByClubIdAndStatus(clubId, "active");
    }

    @Override
    @Transactional(readOnly = true)
    public Club getClubById(Integer clubId) {
        log.info("Fetching club with ID: {}", clubId);
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with ID: " + clubId));
    }

    @Override
    public Club createClub(Club club) {
        log.info("Creating new club with name: {}", club.getClubName());
        return clubRepository.save(club);
    }

    @Override
    public void deleteClub(Integer clubId) {
        log.info("Deleting club with ID: {}", clubId);
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with ID: " + clubId));
        clubRepository.delete(club);
    }

    private ClubDashboardDTO buildClubDashboardDTO(Club club) {
        long membersCount = clubMemberRepository.countByClubIdAndStatus(club.getClubId(), "active");
        long joinRequestsCount = clubMemberRepository.countByClubIdAndStatus(club.getClubId(), "pending");
        long leadershipAppsCount = leadershipApplicationRepository.countByClubId(club.getClubId());

        return ClubDashboardDTO.builder()
                .clubId(club.getClubId())
                .clubName(club.getClubName())
                .type(club.getCategory())
                .membersCount(membersCount)
                .joinRequestsCount(joinRequestsCount)
                .leadershipAppsCount(leadershipAppsCount)
                .createdDate(club.getCreatedAt())
                .build();
    }
}
