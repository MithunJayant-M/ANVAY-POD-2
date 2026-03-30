export interface Event {
  eventId: number;
  institutionId: number;
  clubId?: number;
  eventTitle: string;
  description?: string;
  category: string;
  eventDate: string;
  eventTime?: string;
  location: string;
  capacity?: number;
  registeredCount?: number;
  status: 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';
  createdDate?: string;
  updatedDate?: string;
}

export interface EventCreateRequest {
  institutionId: number;
  clubId?: number;
  eventTitle: string;
  description?: string;
  category: string;
  eventDate: string;
  eventTime?: string;
  location: string;
  capacity?: number;
}

export interface EventParticipant {
  participantId: number;
  eventId: number;
  userId: number;
  registeredDate: string;
  status: 'REGISTERED' | 'ATTENDED' | 'CANCELLED';
}

export interface EventResponse {
  eventId: number;
  institutionId: number;
  clubId?: number;
  eventTitle: string;
  description?: string;
  category: string;
  eventDate: string;
  eventTime?: string;
  location: string;
  capacity?: number;
  registeredCount?: number;
  status: 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';
  createdDate?: string;
  updatedDate?: string;
}

export interface DashboardStats {
  totalEvents: number;
  totalClubs: number;
  totalStudents: number;
  totalRegistrations: number;
}
