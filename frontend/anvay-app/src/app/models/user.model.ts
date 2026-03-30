export interface User {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: 'institution' | 'student' | 'super_admin';
  institutionId?: number;
  createdDate?: string;
  updatedDate?: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  role: string;
  institutionId?: number;
}
