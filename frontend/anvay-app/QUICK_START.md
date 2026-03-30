# 🚀 Anvay Angular Frontend - Quick Start Guide

## ✅ Pre-Requisites Checklist

Before running the application, ensure you have:

- [ ] Node.js 18+ installed
- [ ] npm 9+ installed
- [ ] Angular CLI 17+ globally installed: `npm install -g @angular/cli@17`
- [ ] Backend running on `http://localhost:9092`
- [ ] MySQL database configured
- [ ] Git installed (optional)

---

## 📥 Installation Steps

### 1. Install Frontend Dependencies

```bash
cd d:\Anvay\ANVAY-POD-2\frontend\anvay-app
npm install
```

**Expected Duration:** 2-5 minutes

**Verify Installation:**
```bash
ng version
```

Should show Angular 17.x.x

### 2. Start Development Server

```bash
npm start
```

**Expected Output:**
```
✔ Compiled successfully.
You can now view anvay-app in the browser.

  Local:            http://localhost:4200/
  On Your Network:  http://YOUR_IP:4200/  
```

### 3. Verify Backend Connection

Open browser console (F12) and navigate to:
```
http://localhost:4200/dashboard/institution
```

Check for these success indicators:
- [ ] Page loads without 404 errors
- [ ] Network tab shows successful API calls to localhost:9092
- [ ] Dashboard cards are visible
- [ ] No red errors in console (warnings are OK)

---

## 🧪 Basic Testing

### Test 1: Dashboard Loads
1. Navigate to `http://localhost:4200/dashboard/institution`
2. You should see:
   - Header with "Institution Dashboard" title
   - 4 Statistics cards (Events, Clubs, Students, Registrations)
   - Upcoming events section
   - Event management table
   - Club overview

**Expected:** ✅ All sections visible

### Test 2: API Data Loads
1. Open browser DevTools (F12)
2. Go to Network tab
3. Look for these API calls:
   - `GET .../api/events/institution/1/stats` ✅ Success
   - `GET .../api/events/institution/1/upcoming` ✅ Success (may be 404, then fallback)
   - `GET .../api/events/institution/1` ✅ Success
   - `GET .../api/clubs/institution/1` ✅ Success

**Expected:** ✅ 2xx status codes (200, 201) or handled 404s

### Test 3: Create Event Modal
1. Click "Create Event" button (green button, top right)
2. Modal should appear with form
3. Try filling in the form:
   - Title: "Test Event"
   - Category: "SPORTS"
   - Date: Today's date or future
   - Location: "Test Location"
4. Click "Create Event"

**Expected:** 
- ✅ Form submits without errors
- ✅ Success message or event appears in table
- ✅ Modal closes

### Test 4: Event Filtering
1. In Event Management table, type in search box: "test"
2. Dropdown filters should work
3. Table should update

**Expected:** ✅ Filters update table in real-time

### Test 5: Responsive Design
1. Open DevTools (F12)
2. Toggle device toolbar (Ctrl+Shift+M)
3. Select mobile device (iPhone 12, etc.)
4. Reload page

**Expected:**
- ✅ Layout adapts to mobile screen
- ✅ Cards stack vertically
- ✅ Buttons are full width
- ✅ Table is readable (may scroll horizontally)

---

## 🔍 Debugging Common Issues

### Issue: "Cannot connect to localhost:9092"

**Symptoms:**
```
GET http://localhost:9092/api/events... ERR_CONNECTION_REFUSED
```

**Solution:**
1. Verify backend is running:
   ```bash
   netstat -ano | findstr :9092
   ```
2. If not running, start backend:
   ```bash
   cd d:\Anvay\ANVAY-POD-2\anvay\anvay
   .\mvnw.cmd spring-boot:run
   ```
3. Wait for message: "Tomcat started on port(s): 9092"

### Issue: "Blank Dashboard"

**Symptoms:** Page loads but no data visible

**Debugging:**
1. Open browser console (F12)
2. Check for errors like:
   - 401 Unauthorized → Check authentication
   - 404 Not Found → API endpoint doesn't exist
   - 500 Server Error → Backend issue
3. Check Network tab for API responses
4. Hard refresh (Ctrl+Shift+R)

### Issue: "Modal Not Opening"

**Symptoms:** Create Event button clicked but nothing happens

**Debugging:**
1. Open console: `console.log('test')`
2. Check DevTools Network tab during click
3. Look for JavaScript errors in console
4. Verify `show-event-modal` signal is toggling

### Issue: "Form Won't Submit"

**Symptoms:** Submit button disabled or error on click

**Debugging:**
1. Check form validation:
   - Title has at least 3 characters
   - Category is selected
   - Date is today or future
   - Location has at least 3 characters
2. Check browser console for validation errors
3. Fill in required fields (marked with *)

### Issue: "Styles Look Wrong"

**Symptoms:** Colors, spacing, or layout doesn't match

**Solutions:**
1. Clear browser cache: Ctrl+Shift+Delete
2. Hard refresh: Ctrl+Shift+R
3. Check DevTools > Styles for overrides
4. Verify CSS files exist:
   ```bash
   ls -la src/app/dashboard/*/**.css
   ```

---

## 📊 Expected API Responses

### GET /api/events/institution/{id}/stats
```json
{
  "totalEvents": 5,
  "totalClubs": 3,
  "totalStudents": 25,
  "totalRegistrations": 42
}
```

### GET /api/events/institution/{id}
```json
[
  {
    "eventId": 1,
    "eventTitle": "Tech Summit 2024",
    "category": "TECHNICAL",
    "eventDate": "2024-04-15",
    "location": "Main Hall",
    "registeredCount": 120,
    "capacity": 150,
    "status": "UPCOMING"
  }
]
```

### GET /api/clubs/institution/{id}
```json
[
  {
    "clubId": 1,
    "clubName": "Tech Club",
    "type": "TECHNICAL",
    "membersCount": 45,
    "joinRequestsCount": 3,
    "leadershipAppsCount": 2,
    "createdDate": "2024-01-15T10:00:00"
  }
]
```

---

## 🛠️ Useful Commands

### Development
```bash
# Start dev server
npm start

# Build for production
npm run build

# Run tests
npm test

# Check code quality
npm run lint
```

### Troubleshooting
```bash
# Clear node modules and reinstall
rm -r node_modules package-lock.json
npm install

# Check Angular version
ng version

# Generate component (example)
ng generate component dashboard/new-component

# Serve production build locally
npm run build
npx http-server dist/anvay-app
```

---

## 📝 File Locations Reference

| Component | Files |
|-----------|-------|
| Models | `src/app/models/*.ts` |
| Services | `src/app/services/*.ts` |
| Guards | `src/app/guards/*.ts` |
| Institution Dashboard | `src/app/dashboard/institution/*` |
| Event Management | `src/app/dashboard/event-management/*` |
| Create Event Modal | `src/app/dashboard/event-management/create-event-modal/*` |

---

## ✨ Tips & Tricks

### Speed Up Loading
```typescript
// In network tab, look for these files
// They should be cached on subsequent loads:
- main.js (150-300KB)
- polyfills.js (20-40KB)
- styles.css (50-100KB)
```

### Access Current User
```typescript
// In any service
this.authService.getCurrentUser() // Returns User object
this.authService.getToken()      // Returns JWT token
```

### Monitor API Calls
```typescript
// In browser console
// Check all pending requests
window.performance.getEntriesByType('resource')
```

### Force Refresh Data
```typescript
// In dashboard component
this.refresh() // Reloads all data from backend
```

---

## 🔒 Security Notes

- ✅ JWT token stored in localStorage
- ✅ Token sent in Authorization header
- ✅ Routes protected with auth guard
- ✅ Role-based access control
- ⚠️ Don't commit tokens to git
- ⚠️ HTTPS required in production

---

## 📞 Support

If you encounter issues:

1. **Check logs:** Browser console (F12) and backend logs
2. **Verify backend:** `curl http://localhost:9092/api/events`
3. **Check network:** DevTools > Network tab
4. **Review code:** Check component implementation
5. **Rebuild:** `npm run build`

---

## ✅ Success Criteria

Your setup is **successful** when:

- ✅ `npm start` runs without errors
- ✅ Page loads at `http://localhost:4200`
- ✅ Dashboard shows 4 statistics cards
- ✅ Event table displays events
- ✅ Create Event modal opens and closes
- ✅ All filters work
- ✅ No errors in browser console
- ✅ Responsive design works on mobile

---

**Ready to test!** 🎉

Next step: Open `http://localhost:4200/dashboard/institution` in your browser.
