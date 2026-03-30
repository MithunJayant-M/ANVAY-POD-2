# Anvay Angular Frontend - Implementation Guide

## 📋 Overview

Complete Angular 17 frontend implementation for the Anvay campus management system. This includes:
- **Institution Dashboard** - Main overview page with statistics, upcoming events, and clubs
- **Event Management** - Dynamic table with filtering, search, and CRUD operations
- **Create Event Modal** - Reactive form for creating new events

---

## 📁 Project Structure

```
src/app/
├── models/
│   ├── user.model.ts          # User, AuthResponse interfaces
│   ├── event.model.ts         # Event, EventCreateRequest, DashboardStats
│   └── club.model.ts          # Club, ClubDashboard interfaces
├── services/
│   ├── auth.service.ts        # Authentication service
│   ├── event.service.ts       # Event API service
│   └── club.service.ts        # Club API service
├── guards/
│   └── auth.guard.ts          # Route protection guard
├── dashboard/
│   ├── institution/
│   │   ├── institution.component.ts
│   │   ├── institution.component.html
│   │   └── institution.component.css
│   └── event-management/
│       ├── event-management.component.ts
│       ├── event-management.component.html
│       ├── event-management.component.css
│       └── create-event-modal/
│           ├── create-event-modal.component.ts
│           ├── create-event-modal.component.html
│           └── create-event-modal.component.css
├── app.routes.ts
└── app.config.ts
```

---

## 🔧 Models & Interfaces

### User Model (`user.model.ts`)
```typescript
interface User {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: 'institution' | 'student' | 'super_admin';
  institutionId?: number;
  createdDate?: string;
  updatedDate?: string;
}
```

### Event Model (`event.model.ts`)
```typescript
interface Event {
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

interface DashboardStats {
  totalEvents: number;
  totalClubs: number;
  totalStudents: number;
  totalRegistrations: number;
}
```

### Club Model (`club.model.ts`)
```typescript
interface ClubDashboard {
  clubId: number;
  clubName: string;
  type: string;
  membersCount: number;
  joinRequestsCount: number;
  leadershipAppsCount: number;
  createdDate: string;
}
```

---

## 📡 Services

### AuthService (`auth.service.ts`)
- `login(credentials)` - User login
- `signup(data)` - User registration
- `logout()` - Clear session
- `isAuthenticated()` - Check if user is logged in
- `hasRole(role)` - Check user role
- Uses localStorage for persistence
- BehaviorSubjects for reactive updates

### EventService (`event.service.ts`)
**Base URL:** `http://localhost:9092/api/events`

**Methods:**
- `getEventsByInstitution(institutionId)` - GET all events for institution
- `getAllEvents()` - GET all events
- `getEventById(eventId)` - GET single event
- `createEvent(event)` - POST new event
- `updateEvent(eventId, event)` - PUT update event
- `deleteEvent(eventId)` - DELETE event
- `getEventParticipants(eventId)` - GET event participants
- `getUpcomingEvents(institutionId)` - GET upcoming events
- `getDashboardStats(institutionId)` - GET statistics
- `registerForEvent(eventId, userId)` - Register user for event
- `unregisterFromEvent(eventId, userId)` - Unregister user

### ClubService (`club.service.ts`)
**Base URL:** `http://localhost:9092/api/clubs`

**Methods:**
- `getClubsByInstitution(institutionId)` - GET clubs for institution
- `getClubById(clubId)` - GET single club
- `getClubMembers(clubId)` - GET club members
- `getApprovedMembers(clubId)` - GET approved members
- `createClub(club)` - POST new club
- `updateClub(clubId, club)` - PUT update club
- `deleteClub(clubId)` - DELETE club

---

## 🔐 Guards

### AuthGuard (`auth.guard.ts`)
Protects routes that require authentication. Checks:
- User is logged in (token exists)
- User has required role (if role is specified in route data)

**Usage in routes:**
```typescript
{
  path: 'dashboard/institution',
  component: InstitutionComponent,
  canActivate: [authGuard],
  data: { role: 'institution' }
}
```

---

## 📄 Components

### 1. Institution Component (Dashboard)

**File:** `institution.component.ts`

**Signals:**
- `dashboardStats` - Statistics data
- `upcomingEvents` - Upcoming events list
- `clubs` - Club list
- `loading` - Loading state
- `showCreateEventModal` - Modal visibility
- `error` - Error messages

**Methods:**
- `ngOnInit()` - Load dashboard data on component init
- `loadDashboardData()` - Fetch stats, events, and clubs
- `openCreateEventModal()` - Show create event modal
- `closeCreateEventModal()` - Hide modal
- `onEventCreated(event)` - Handle event creation
- `refresh()` - Reload dashboard data

**Template Features:**
- Header with title and action buttons
- Error alert display
- Loading spinner
- 4 Statistics cards (grid layout)
- Upcoming events preview (max 3 cards)
- Event management table component
- Club overview grid
- Create event modal

**Styling:**
- Responsive grid layouts
- Gradient backgrounds
- Hover animations
- Mobile-optimized (768px breakpoint)

### 2. Event Management Component

**File:** `event-management.component.ts`

**Inputs:**
- `@Input() institutionId: number`

**Signals:**
- `events` - Events list
- `clubs` - Clubs for filter dropdown
- `loading` - Loading state
- `selectedClubFilter` - Selected club filter
- `selectedStatusFilter` - Selected status filter
- `searchTerm` - Search text

**Methods:**
- `loadEvents()` - Fetch events from API
- `loadClubs()` - Fetch clubs for filtering
- `getFilteredEvents()` - Apply filters and search
- `getCategoryBadgeClass(category)` - Get CSS class for category
- `getStatusLabel(status)` - Get status display text
- `editEvent(event)` - Edit event handler
- `deleteEvent(eventId)` - Delete event with confirmation
- `refresh()` - Reload events
- `clearFilters()` - Reset all filters

**Template Features:**
- Search box
- Filter dropdowns (Status, Club)
- Sortable table columns:
  - Event Title
  - Category Badge (6 categories with colors)
  - Date (formatted)
  - Location
  - Participants (with capacity)
  - Status Badge (4 statuses with colors)
  - Actions (Edit, Delete)
- Clear filters button
- Events summary
- Empty state message

**Table Columns:**
| Column | Type | Features |
|--------|------|----------|
| Title | Text | Bold font |
| Category | Badge | 6 color-coded categories |
| Date | Date | Formatted as "MMM dd, yyyy" |
| Location | Text | Location name |
| Participants | Number | Current / Max capacity |
| Status | Badge | 4 color-coded statuses |
| Actions | Buttons | Edit (✎) and Delete (✕) |

### 3. Create Event Modal Component

**File:** `create-event-modal.component.ts`

**Inputs:**
- `@Input() institutionId: number`

**Outputs:**
- `@Output() close` - Emitted when modal closes
- `@Output() eventCreated` - Emitted when event is created

**Form Controls:**
- `eventTitle` - Required, min 3 chars
- `description` - Optional, max 500 chars
- `category` - Required dropdown
- `eventDate` - Required, date picker (min: today)
- `eventTime` - Optional, time picker
- `location` - Required, min 3 chars
- `capacity` - Optional number, min 1
- `clubId` - Optional, club dropdown

**Validation:**
- Real-time error messages
- Field-level validation
- Form-level submit validation
- Min date set to today

**Form Actions:**
- Cancel button - Close modal
- Submit button - Create event (disabled if form invalid)
- Loading spinner during submission

**Signals:**
- `eventForm` - FormGroup
- `clubs` - Available clubs
- `loading` - Loading clubs state
- `submitting` - Submitting form state
- `error` - Error message
- `success` - Success state

**Categories:**
- SPORTS
- ACADEMIC
- SOCIAL
- CULTURAL
- TECHNICAL
- OTHER

**Modal Features:**
- Overlay with backdrop click detection
- Smooth animations (fadeIn, slideUp)
- Success/Error alerts
- Loading spinner
- Close button (×)
- Responsive design

---

## 🚀 Usage

### Running the Application

1. **Start the backend** (if not already running):
   ```bash
   cd d:\Anvay\ANVAY-POD-2\anvay\anvay
   .\mvnw.cmd spring-boot:run
   ```
   Backend will be available at: `http://localhost:9092`

2. **Start the Angular frontend**:
   ```bash
   cd d:\Anvay\ANVAY-POD-2\frontend\anvay-app
   npm install
   npm start
   ```
   Frontend will be available at: `http://localhost:4200`

3. **Access the dashboard**:
   - Navigate to `http://localhost:4200/dashboard/institution`
   - Login if not authenticated
   - View dashboard with statistics, events, and clubs

### Creating an Event

1. Click "Create Event" button in dashboard header
2. Fill in the form:
   - Event Title (required)
   - Description (optional)
   - Category (required)
   - Date (required, cannot be past)
   - Time (optional)
   - Location (required)
   - Capacity (optional)
   - Club (optional)
3. Click "Create Event" to submit
4. Modal closes on success and dashboard refreshes

### Filtering Events

1. In Event Management table:
   - Search by title, location, or description
   - Filter by Status (Upcoming, Ongoing, Completed, Cancelled)
   - Filter by Club
2. Click "Clear Filters" to reset

---

## 🎨 Styling & Theming

### Color Scheme
- Primary: #4CAF50 (Green)
- Secondary: #2196F3 (Blue)
- Danger: #f44336 (Red)
- Warning: #ff9800 (Orange)
- Success: #4CAF50 (Green)

### Category Badge Colors
- SPORTS: Red (#c62828)
- ACADEMIC: Blue (#1565c0)
- SOCIAL: Purple (#6a1b9a)
- CULTURAL: Orange (#e65100)
- TECHNICAL: Teal (#004d40)
- OTHER: Grey (#666)

### Status Badge Colors
- UPCOMING: Blue (#1976d2)
- ONGOING: Orange (#f57c00)
- COMPLETED: Green (#388e3c)
- CANCELLED: Red (#d32f2f)

---

## 📱 Responsive Breakpoints

- **Desktop**: 1200px+
- **Tablet**: 768px - 1199px
- **Mobile**: < 768px

**Layout Changes:**
- Dashboard header stacks on mobile
- Grid becomes single column
- Table scrolls horizontally
- Modal takes full width (with padding)
- Buttons stack vertically in modal footer

---

## 🔄 API Integration

### Expected Backend Endpoints

The frontend expects these endpoints to be available:

**Events:**
- `GET /api/events`
- `GET /api/events/{id}`
- `GET /api/events/institution/{id}`
- `GET /api/events/institution/{id}/stats`
- `GET /api/events/institution/{id}/upcoming`
- `POST /api/events`
- `PUT /api/events/{id}`
- `DELETE /api/events/{id}`

**Clubs:**
- `GET /api/clubs`
- `GET /api/clubs/{id}`
- `GET /api/clubs/institution/{id}`
- `POST /api/clubs`
- `PUT /api/clubs/{id}`
- `DELETE /api/clubs/{id}`

**Authentication:**
- `POST /api/auth/login`
- `POST /api/auth/signup`

---

## ✅ Testing Checklist

- [ ] Dashboard loads without errors
- [ ] Statistics cards display correct numbers
- [ ] Upcoming events display in preview
- [ ] Event management table loads events
- [ ] Filters work correctly (search, status, club)
- [ ] Create event modal opens/closes
- [ ] Form validation works
- [ ] Event creation succeeds
- [ ] Error messages display on API failures
- [ ] Delete event confirmation works
- [ ] Responsive design on mobile devices
- [ ] localStorage persists authentication

---

## 🐛 Troubleshooting

### Events not loading
- Check backend is running on port 9092
- Verify `/api/events/institution/{id}` endpoint exists
- Check browser console for HTTP errors
- Check authentication token is valid

### Modal not opening
- Check `showCreateEventModal` signal is toggling
- Verify `EventManagementComponent` is imported
- Check modal CSS z-index (should be 1000+)

### Form validation not working
- Verify `ReactiveFormsModule` is imported
- Check form control names match template
- Verify validators are properly applied

### Styles not applying
- Check Angular CSS encapsulation
- Verify CSS files are in correct locations
- Check for CSS selector conflicts
- Clear browser cache and rebuild

---

## 🚀 Future Enhancements

- [ ] Pagination for event table
- [ ] Batch operations (multi-select delete)
- [ ] Event edit functionality
- [ ] Real-time updates with WebSocket
- [ ] Export events to CSV
- [ ] Calendar view for events
- [ ] Event notifications
- [ ] User preferences/settings
- [ ] Dark theme
- [ ] Internationalization (i18n)

---

## 📚 Dependencies

- Angular 17
- RxJS 7.8
- Angular Forms (Reactive Forms)
- Angular Common

---

## 📄 License

This is part of the Anvay campus management system.

---

**Last Updated:** March 31, 2026
**Version:** 1.0.0
