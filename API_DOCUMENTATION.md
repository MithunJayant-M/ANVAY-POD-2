# Anvay — API Documentation

Reference for the Anvay backend REST API. The authoritative interactive spec is served by the deployed backend itself via SpringDoc/OpenAPI:

| Resource | URL |
| --- | --- |
| **Swagger UI** (interactive) | `https://anvay-pod-2.onrender.com/swagger-ui.html` |
| **OpenAPI JSON** (machine-readable spec) | `https://anvay-pod-2.onrender.com/v3/api-docs` |
| **Local Swagger UI** (dev) | `http://localhost:8081/swagger-ui.html` |
| **Local OpenAPI JSON** (dev) | `http://localhost:8081/v3/api-docs` |

Generate a client SDK or import the spec into Postman/Insomnia by pointing at the `v3/api-docs` URL.

---

## Base URL

| Environment | Base URL |
| --- | --- |
| Production | `https://anvay-pod-2.onrender.com` |
| Local | `http://localhost:8081` |
| Dev proxy (Angular `npm start`) | Same-origin `/api/**` is forwarded to local backend by `proxy.conf.json` |

---

## Authentication

The API uses **JWT bearer tokens**. Obtain a token from `POST /api/auth/login`, then attach it to every subsequent request:

```http
Authorization: Bearer <token>
```

Tokens expire after `app.jwt.expiration-ms` (default 24 h). Sessions are stateless — there is no refresh-token endpoint; clients re-login on expiry.

### Endpoints that don't require auth
- `POST /api/auth/login`
- `POST /api/auth/register/institution`
- `POST /api/auth/register/student`
- `POST /api/auth/reset-password`
- `GET /api/institutions/active`
- All Swagger / OpenAPI / Actuator routes (`/swagger-ui/**`, `/v3/api-docs/**`, `/webjars/**`, `/actuator/**`)

### Role-based authorization
| Path prefix | Required role |
| --- | --- |
| `/api/super-admin/**` | `SUPER_ADMIN` |
| Everything else (non-public) | Any authenticated user |

Roles in the DB (`users.role` column, all lowercase):

| Role string | Where assigned |
| --- | --- |
| `super_admin` | Seeded once on first boot ([DataInitializer.java:36](ANVAY-POD-2/anvay/anvay/src/main/java/com/cts/mfrp/anvay/config/DataInitializer.java#L36)) |
| `student` | Default for new student registrations ([UserServiceImpl.java:94](ANVAY-POD-2/anvay/anvay/src/main/java/com/cts/mfrp/anvay/service/impl/UserServiceImpl.java#L94)) |
| `club_leader` | Promoted via `PUT /api/leadership-applications/{id}/approve` ([LeadershipApplicationController.java:49](ANVAY-POD-2/anvay/anvay/src/main/java/com/cts/mfrp/anvay/controller/LeadershipApplicationController.java#L49)) — demoted back to `student` on club deletion or via `DELETE /api/clubs/{clubId}/leader/{userId}` |

Spring Security's `hasRole("SUPER_ADMIN")` ([SecurityConfig.java:56](ANVAY-POD-2/anvay/anvay/src/main/java/com/cts/mfrp/anvay/security/SecurityConfig.java#L56)) compares against `ROLE_<role>` — the actual mapping between the lowercase DB value and Spring's uppercase authority happens in `CustomUserDetailsService`. Institution-admin role (used for `/api/auth/register/institution` flow) is created by that endpoint — I haven't traced the exact role string used there; verify against `AuthServiceImpl` or the actual seeded admin's `role` column.

---

## Response shapes

### Standard error
Returned by `GlobalExceptionHandler` for unhandled exceptions:

```json
{
  "status": 500,
  "message": "An unexpected error occurred. Please try again later.",
  "timestamp": 1715000000000
}
```

Set the Render env var `DEBUG_EXPOSE_ERRORS=true` to expose the real exception class + message in the `message` field (debugging only — leave off in production).

### Paginated response (Spring Page<T>)
Any endpoint suffixed `/page` returns this shape:

```json
{
  "content": [ /* array of T */ ],
  "number": 0,            // current page index (0-based)
  "size": 20,
  "totalElements": 47,
  "totalPages": 3,
  "first": true,
  "last": false,
  "sort": { ... },
  "pageable": { ... }
}
```

Pagination query params: `?page=0&size=20&sort=startDate,desc&sort=eventName,asc`

---

## Endpoints

### Auth — `/api/auth` (no token required)

| Method | Path | Body | Description |
| --- | --- | --- | --- |
| POST | `/login` | `{ email, password }` | Returns `{ token, user }` |
| POST | `/register/institution` | `RegisterInstitutionRequest` | Creates pending institution + admin |
| POST | `/register/student` | `RegisterStudentRequest` | Creates student in an active institution |
| POST | `/reset-password` | `{ email, newPassword }` | Reset password by email |

---

### Institutions — `/api/institutions`

| Method | Path | Description |
| --- | --- | --- |
| GET | `/` | All institutions (auth) |
| GET | `/leaderboard` | Institutions ordered by total points |
| GET | `/active` | All approved/active institutions (**public**) |
| GET | `/{institutionId}` | Single institution |
| PUT | `/{institutionId}` | Update institution |
| DELETE | `/{institutionId}` | Delete institution |

---

### Super Admin — `/api/super-admin` (role: `SUPER_ADMIN`)

| Method | Path | Description |
| --- | --- | --- |
| GET | `/dashboard` | Platform-wide stats (`DashboardStatsDto`) |
| GET | `/institutions?search=...` | All institutions with stats |
| GET | `/institutions/{id}` | Single institution detail |
| GET | `/institutions/{id}/events` | Events at this institution |
| GET | `/institutions/{id}/clubs` | Clubs at this institution |
| PUT | `/institutions/{id}/approve` | Activate a pending institution |
| PUT | `/institutions/{id}/deactivate` | Deactivate an institution |
| GET | `/analytics` | Platform analytics + rankings |

---

### Students — `/api/students`

| Method | Path | Returns | Notes |
| --- | --- | --- | --- |
| GET | `/{id}/dashboard` | `StudentDashboardDTO` | Includes `joinedClubs`, `upcomingEvents`, points, rank |
| GET | `/{id}/profile` | `User` | Full profile (lazy collections excluded) |
| GET | `/institution/{institutionId}/leaderboard` | `List<StudentSummaryDTO>` | DTO — no `profilePicture`, ordered by points DESC |
| GET | `/leaderboard` | `List<StudentSummaryDTO>` | Global leaderboard |
| GET | `/` | `List<User>` | All students — heavy, avoid in prod |
| GET | `/page` | `Page<StudentSummaryDTO>` | **Preferred** — paginated, light |
| GET | `/institution/{institutionId}/page` | `Page<StudentSummaryDTO>` | Paginated institution students |
| DELETE | `/{id}` | `204 No Content` | Soft-removes the student from their institution |

---

### Events — `/api/events`

| Method | Path | Returns | Notes |
| --- | --- | --- | --- |
| GET | `/` or `""` | `List<EventSummaryDTO>` | All events, DTO (no `imageData`) |
| GET | `/page` | `Page<EventSummaryDTO>` | **Preferred** for large datasets |
| GET | `/institution/{institutionId}` | `List<EventSummaryDTO>` | DTO list |
| GET | `/institution/{institutionId}/page` | `Page<EventSummaryDTO>` | Paginated |
| GET | `/{eventId}` | `Event` | Full event including `imageData`, `eventRules` |
| GET | `/club/{clubId}` | `List<Event>` | Events created by a club |
| POST | `/` | `Event` | Create event (institution_admin / club_leader) |
| PUT | `/{eventId}` | `Event` | Update event |
| DELETE | `/{eventId}` | `204` | Delete event |
| GET | `/all?userId=...` | `List<EventFeedDTO>` | Feed view with registration status |
| GET | `/feed?userId=...&institutionId=...` | `List<EventFeedDTO>` | Filtered student feed |
| POST | `/register` | `EventParticipant` | Register a participant |
| GET | `/{eventId}/participants` | `List<EventParticipant>` | Roster |
| POST | `/{eventId}/award-winners` | `{ message }` | Submit `{ firstUserId, secondUserId, thirdUserId }` for approval |
| POST | `/{eventId}/approve-winners` | `{ message }` | Super-admin approval, awards points |
| GET | `/pending-winners` | `List<WinnersApprovalDTO>` | Pending approval queue |
| PUT | `/{eventId}/end` | `{ message }` | Mark event ended |
| GET | `/my-registrations?userId=...` | `List<EventFeedDTO>` | Logged-in student's registrations |

---

### Clubs — `/api/clubs`

| Method | Path | Returns | Notes |
| --- | --- | --- | --- |
| GET | `/all` | `List<Club>` | All clubs |
| GET | `/institution/{institutionId}` | `List<ClubDashboardDTO>` | Clubs + counts for institution dashboard |
| GET | `/{clubId}` | `Club` | Single club |
| PUT | `/{clubId}` | `Club` | Update club |
| POST | `/` | `Club` | Create club |
| DELETE | `/{clubId}` | `204` | Delete club (cascades: leaders demoted to student) |
| GET | `/user/{userId}` | `List<ClubMember>` | All memberships for a user |

#### Member management

| Method | Path | Returns | Notes |
| --- | --- | --- | --- |
| GET | `/{clubId}/members` | `List<ClubMemberSummaryDTO>` | All members of club |
| GET | `/{clubId}/members/approved` | `List<ClubMemberSummaryDTO>` | APPROVED only |
| GET | `/{clubId}/join-requests` | `List<ClubMemberSummaryDTO>` | PENDING only |
| POST | `/{clubId}/join` | `ClubMember` (201) | Student requests to join. 409 if already a member/pending |
| PUT | `/{clubId}/join-requests/{memberId}/approve` | `ClubMemberSummaryDTO` | 400 if already approved (idempotent) |
| PUT | `/{clubId}/join-requests/{memberId}/reject` | `ClubMemberSummaryDTO` | 400 if already rejected |
| DELETE | `/{clubId}/members/{memberId}` | `204` | Remove member, decrement user counter |
| DELETE | `/{clubId}/leader/{userId}` | `200` | Demote leader back to student |

---

### Leadership Applications — `/api/leadership-applications`

| Method | Path | Description |
| --- | --- | --- |
| POST | `/` | Apply to lead a club |
| GET | `/club/{clubId}` | All applications for a club |
| GET | `/club/{clubId}/pending` | Pending only |
| GET | `/user/{userId}` | Applications by a user |
| PUT | `/{id}/approve` | Approve — promotes user to `club_leader` |
| PUT | `/{id}/reject` | Reject |

---

### Achievements — `/api/achievements`

| Method | Path | Description |
| --- | --- | --- |
| GET | `/{achievementId}` | Single achievement |
| GET | `/user/{userId}` | All achievements for a user |

---

### Members (generic users) — `/api/members`

| Method | Path | Description |
| --- | --- | --- |
| GET | `/` | All users |
| GET | `/{memberId}` | Single user |
| PUT | `/{memberId}` | Update user |

---

### Chat — `/api/chat`

| Method | Path | Description |
| --- | --- | --- |
| POST | `/` | Rule-based chatbot; body `{ message }`, returns `{ reply }` |

Responses configured in `src/main/resources/chatbot_responses.json` (restart required to apply edits).

---

## Common request examples

### 1. Login
```bash
curl -X POST https://anvay-pod-2.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@anvay.com","password":"Admin@123"}'
```

Response (flat — defined in `LoginResponse.java`):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "super_admin",
  "name": "Super Admin",
  "userId": 1,
  "institutionId": null,
  "leadingClubId": null
}
```

### 2. Authenticated request
```bash
TOKEN="eyJhbGciOi..."
curl https://anvay-pod-2.onrender.com/api/students/1/dashboard \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Paginated list with sort
```bash
curl "https://anvay-pod-2.onrender.com/api/events/page?page=0&size=10&sort=startDate,desc" \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Approve a join request
```bash
curl -X PUT "https://anvay-pod-2.onrender.com/api/clubs/5/join-requests/42/approve" \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Register a new student
Body matches `RegisterStudentRequest.java` — single `name` field (not firstName/lastName):
```bash
curl -X POST https://anvay-pod-2.onrender.com/api/auth/register/student \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Jane Doe",
    "email":"jane@example.edu",
    "password":"S3cure!",
    "institutionId":3,
    "studentIdNumber":"STU2024-001"
  }'
```

---

## Notes for frontend integration

- The frontend uses an Angular `HttpInterceptor` (`apiPrefixInterceptor`) that prepends `environment.apiBaseUrl` to bare `/api/*` paths — works for both single-service and split deployments.
- A second interceptor (`jwtInterceptor`) auto-attaches `Authorization: Bearer <token>` from `AuthService.getToken()`.
- For paginated endpoints, model the response with this TypeScript interface:
  ```typescript
  export interface SpringPage<T> {
    content: T[];
    number: number;        // current page index (0-based)
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
  }
  ```

---

## Status code conventions

| Code | Meaning in this API |
| --- | --- |
| 200 OK | Successful read or update |
| 201 Created | Successful create (POST returning new resource) |
| 204 No Content | Successful delete |
| 400 Bad Request | Validation failure or invalid state transition (e.g., approving an already-approved request) |
| 401 Unauthorized | Missing or invalid JWT |
| 403 Forbidden | Authenticated but role insufficient (e.g., non-SUPER_ADMIN hitting `/api/super-admin/**`) |
| 404 Not Found | Resource doesn't exist |
| 409 Conflict | Duplicate join request, etc. |
| 500 Internal Server Error | Unhandled exception — check Render logs |

---

## Regenerating this document

This file is a curated summary. The complete schema (with request/response DTOs, validation rules, and all parameters) is in the OpenAPI spec. To pull a fresh snapshot:

```bash
curl https://anvay-pod-2.onrender.com/v3/api-docs > openapi.json
# or use the Swagger UI directly to explore and try endpoints
```

When endpoints are added to a controller in `com.cts.mfrp.anvay.controller`, they automatically appear in the live Swagger UI — `springdoc.packages-to-scan=com.cts.mfrp.anvay.controller` handles the scanning. Update this file when new endpoints land that the frontend or external integrators need to know about.
