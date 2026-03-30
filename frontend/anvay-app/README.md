# Anvay - Campus Event Management Platform

A multi-tenant Angular 17 app with **Super Admin**, **Institution**, and **Student** roles.

##  Quick Start

### Prerequisites
- Node.js >= 18
- npm >= 9

### Setup

```bash
# 1. Install dependencies
npm install

# 2. Start development server
npm start
```

Open **http://localhost:4200**

---

## Demo Login Credentials

| Role | Email | Password |
|------|-------|----------|
| Super Admin | admin@anvay.in | Admin@123 |
| Institution | admin@griet.ac.in | Griet@123 |
| Student | ravi@student.griet.ac.in | Student@123 |

> Use the **"Fill credentials"** button on the login page to auto-fill for any role.

---

## Project Structure

```
src/app/
├── auth/
│   ├── login/          # Login page (all 3 roles)
│   └── signup/         # Signup page (student + institution)
├── dashboard/
│   ├── super-admin/    # Super Admin dashboard
│   ├── institution/    # Institution dashboard
│   └── student/        # Student event discovery
├── services/
│   └── auth.service.ts # Mock auth service (replace with real API)
├── guards/
│   └── auth.guard.ts   # Route protection
├── models/
│   └── user.model.ts   # TypeScript interfaces
└── app.routes.ts       # Routing config
```

---

## Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Deep Navy Blue | `#1A4363` | Primary headings, CTAs |
| Forest Green | `#2E8B57` | Success, institution accents |
| Green Light | `#8DC63F` | Brand accent, gradient end |
| Gradient | `#1A4363 → #8DC63F` | Action buttons, bars |
| Success Mint | `#E8F5E9` | Special benefit tags |

---

## Replacing Mock Services with Real APIs

All mock logic is in `src/app/services/auth.service.ts`.

### Login endpoint
Replace the mock `login()` method with:
```typescript
login(req: LoginRequest): Observable<LoginResponse> {
  return this.http.post<LoginResponse>('/api/auth/login', req);
}
```

### Signup endpoint
```typescript
signup(req: SignupRequest): Observable<{message: string}> {
  return this.http.post<{message: string}>('/api/auth/signup', req);
}
```

### Institutions list
```typescript
getInstitutions(): Observable<Institution[]> {
  return this.http.get<Institution[]>('/api/institutions');
}
```

### DB Schema (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS campus_management;
USE campus_management;

CREATE TABLE Institutions (
  institution_id INT AUTO_INCREMENT PRIMARY KEY,
  institution_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE,
  phone VARCHAR(20),
  address TEXT,
  domain VARCHAR(100),
  status VARCHAR(50),
  registered_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50),
  institution_id INT,
  FOREIGN KEY (institution_id) REFERENCES Institutions(institution_id)
);

CREATE TABLE Clubs (
  club_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  institution_id INT,
  FOREIGN KEY (institution_id) REFERENCES Institutions(institution_id)
);
```

---

## Build for Production

```bash
npm run build
# Output: dist/anvay-app/
```

---

## Features Implemented

- [x] Login page with role selector (Super Admin / Institution / Student)
- [x] Student signup with institution dropdown (GRIET, VNRVJIET, YCCE, M Kumaraswamy)
- [x] Institution registration form with domain field
- [x] Route guards protecting dashboards
- [x] Super Admin: Stats, College Management (approve/deactivate), Analytics, Settings
- [x] Institution: Events table, Clubs grid, Stats
- [x] Student: Event discovery with search, Register/unregister, My Registrations, Profile
- [x] Mock auth service (localStorage token, swap for real API)
- [x] Anvay color theme (#1A4363 navy + #8DC63F green gradient)
