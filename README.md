# Lectrium

**Lectrium** is a modern, feature-rich Android application built specifically for **college students
** to manage their schedules, class reminders, assignments, projects, exams, attendance tracking,
and academic performance.

College academic life differs significantly from Junior/Senior High School systems—with irregular class blocks, varying day schedules, credit units, and unique attendance dynamics. Lectrium is tailored specifically to address these college workflows.

The project takes inspiration from the open-source app [Lectro](https://github.com/Pankaj-Meharchandani/Lectro), extending its core ideas with an expanded feature set, advanced customization, and an improved user interface and user experience (UI/UX) built with modern Jetpack Compose and Material Design 3.

---

## 📍 Feature Implementation Tracker

> **Legend**: `[x]` Implemented | `[/]` In Progress | `[ ]` Planned

### ⚙️ App Core & Architecture
- `[x]` **Jetpack Compose UI Framework & Material 3 Theming**
- `[x]` **Theme & Color Scheme Customization Engine**
  - `[x]` Theme Modes (Follow System / Light Theme / Dark Theme)
  - `[x]` Color Scheme Options (Sky Blue Default, Material You Dynamic Color, Custom Palette)
  - `[x]` Custom ARGB/Hex Color Picker with Live Theme Preview, Preset Swatches, and RGBA Sliders
- `[x]` **Navigation Engine**
  - `[x]` Floating Navigation Dock with Bouncy Drag Physics & Fluid Transition Animations
  - `[x]` Smooth Animated Screen & Subscreen Transitions (`AnimatedContent` Fade/Scale)
  - `[x]` Liquid Spring Expansion FAB Transition alongside Floating Dock
- `[x]` **User Profile & Legal Management**
  - `[x]` **5-Step Interactive Onboarding Walkthrough**
  - `[x]` **Dedicated Manage Profile Sub-Screen** (`ManageProfileScreen`) with Given Name, Middle
    Name, & Last Name
  - `[x]` **Privacy Policy Screen** (`PrivacyPolicyScreen` — 100% Local On-Device Storage)
  - `[x]` **Open Source Licenses Screen** (`OpenSourceLicensesScreen`)
  - `[x]` Profile Persistence (DataStore) & Initials Avatar / Profile Picture Fallback
- `[x]` **App Settings & Preferences**
  - `[x]` Master Notifications Toggle
  - `[x]` Selective Notification Categories Bottom Sheet
  - `[x]` Class Reminder Duration Configuration (Presets + Custom Dialog)
  - `[x]` Custom Theme Color Picker Bottom Sheet
  - `[x]` Manage Profile, Privacy Policy, and Open Source Licenses Entries
  - `[x]` App Version Secret Easter Egg (7-tap trigger)
- `[ ]` **Proper Application Icons & Branding** (Custom high-resolution launcher icons, adaptive
  icon suite, and thematic app iconography)
- `[ ]` **Expanded Interactive Popups & Dialogs** (Additional quick-edit dialogs, delete
  confirmations, detail modals, and prompt popups across screens)

### 🔔 Notifications Engine

- `[x]` **Academic Reminder Alarm Scheduler & Broadcast Receiver**
- `[x]` **Class Reminders, Assignment Due, Exam Alerts, and Project Deadline Notifications**
- `[x]` **Master Notification Suppression Logic**
- `[x]` **High-Priority Material 3 Notification Channel Setup**

### 📊 Attendance Tracking System
- `[x]` **Dynamic Unit-Based Attendance Engine** (Calculates absence allowances dynamically for 1-unit, 2-unit, 3-unit courses)
- `[x]` **Live Allowance & Warning Badges** (*Safe*, *Warning*, *Critical*, *Advised to Withdraw*)
- `[x]` **Schedule Integration & Automatic Synchronizations**
- `[ ]` **Attendance Tracker Enhancements & Adjustments** (Adjustments to attendance logging
  workflows, attendance record editing, manual override controls, and custom threshold rules)

### 📅 Class Schedules & Course Management

- `[x]` **Schedule Display Views**
  - `[x]` Column / Daily Timeline View (`DayScheduleScreen`)
  - `[x]` Full Timetable View (`TimetableGrid` & `WeeklyScheduleScreen`)
  - `[x]` Full Calendar Grid View (`CalendarGridView`)
  - `[x]` Flexible Days Layout (Mon–Fri default + Custom Saturday/Sunday toggles)
- `[x]` **Course Metadata**
  - `[x]` Course Code, Course Name, Course Section
  - `[x]` Course Units / Credits (1, 2, 3+ units)
  - `[x]` Course Faculty / Professor Info
  - `[x]` Course Room & Location
- `[x]` **Class Classification & Modalities**
  - `[x]` Lecture vs. Laboratory Classification
  - `[x]` Delivery Modality Tags (Face-to-Face, Asynchronous, Hybrid)
- `[ ]` **Course Schedules Screen Management** (Editing existing schedules, deleting schedules,
  updating time blocks/rooms, and schedule conflict handling)

### 📝 Tasks, Assignments, Projects & Exams

- `[x]` **Assignments Tracker** (Deadlines, Due Dates, Status Filtering, Priority Chips)
- `[x]` **Projects Tracker** (Term Project Deadlines, Milestones Checklist, Team Members, Progress
  Bar)
- `[x]` **Exams Tracker** (Exam Countdown Cards, Venues, Weights %, Study Topic Checklists)
- `[ ]` **Full Academic & Grade Tracker System** (Comprehensive tracking for course grades, GPA
  calculations, term targets, and overall academic progress)
- `[ ]` **Dedicated Exams & Quizzes Screen & Features** (Comprehensive exams & quizzes management
  screen, score logging, venue/room details, and quiz countdowns)
- `[ ]` **Tasks & Assignments Screen Refinements** (Sub-task checklists, submission status
  workflows, priority filtering enhancements, and detailed task views)

### 🔕 Automation & Productivity

- `[x]` **Free Time Period / Gap Checker** (Detecting and highlighting free gaps between consecutive
  class blocks)
- `[x]` **Export Schedule as Image** (`ScheduleImageExporter`)
- `[x]` **In-Class Auto-Mute / Do Not Disturb (DND) Notice Toggle**

### 📱 System Integration & Archiving

- `[x]` **Material 3 Home Screen Widgets**:
  - `[x]` Schedules View Widget
  - `[x]` Daily Schedule View Widget
  - `[x]` Exams Countdown Widget
  - `[x]` Assignments Due Widget
  - `[x]` Projects Due Widget
- `[x]` **Academic Archive Manager** (`ArchiveManager` for semester schedule resets)

---

## 🛠️ Building & Running Lectrium

### 💻 Command Line

```powershell
.\gradlew.bat assembleDebug
```

```bash
./gradlew assembleDebug
```

Generated APK location:

```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🤝 Contributing & Licensing

Released under the **Lectrium Custom Restricted License**.
