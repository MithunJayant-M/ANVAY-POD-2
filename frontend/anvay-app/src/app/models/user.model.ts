export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: string;
  name: string;
  userId: number;
  institutionId: number | null;
}

export interface RegisterInstitutionRequest {
  institutionName: string;
  email: string;
  password: string;
  phone: string;
  address: string;
  adminName: string;
}

export interface RegisterStudentRequest {
  name: string;
  email: string;
  password: string;
  institutionId: number | null;
}
