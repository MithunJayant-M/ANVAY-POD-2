export interface Club {
  clubId: number;
  institutionId: number;
  clubName: string;
  category: string;
  description?: string;
  createdDate?: string;
  updatedDate?: string;
}

export interface ClubDashboard {
  clubId: number;
  clubName: string;
  type: string;
  membersCount: number;
  joinRequestsCount: number;
  leadershipAppsCount: number;
  createdDate: string;
}

export interface ClubMember {
  clubMemberId: number;
  clubId: number;
  userId: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  joinDate?: string;
}

export interface LeadershipApplication {
  applicationId: number;
  clubId: number;
  userId: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  appliedDate?: string;
}
