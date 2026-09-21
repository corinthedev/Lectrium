# Lectrium

> [!WARNING]
> **⚠️ CURRENTLY IN ACTIVE DEVELOPMENT**  
> Lectrium is an ongoing project under active development and is **not yet finished or feature-complete**. Certain screens, features, icons, and workflows are still being built or refined. Please refer to the [Feature Implementation Tracker](#-feature-implementation-tracker) below for details.

**Lectrium** is a modern, feature-rich Android application built specifically for **college students** to manage their schedules, class reminders, assignments, projects, exams, attendance tracking, and academic performance.

College academic life differs significantly from Junior/Senior High School systems—with irregular class blocks, varying day schedules, credit units, and unique attendance dynamics. Lectrium is tailored specifically to address these college workflows.

The project takes inspiration from the open-source app [Lectro](https://github.com/Pankaj-Meharchandani/Lectro), extending its core ideas with an expanded feature set, advanced customization, and an improved user interface and user experience (UI/UX) built with modern Jetpack Compose and Material Design 3.

---

## 📍 Feature Implementation Tracker

> [!NOTE]
> **Legend**: `[x]` Implemented | `[/]` In Progress | `[ ]` Planned / Under Development

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
  - `[x]` **Dedicated Manage Profile Sub-Screen** (`ManageProfileScreen` with Given Name, Middle Name, & Last Name)
  - `[x]` **Privacy Policy Screen** (`PrivacyPolicyScreen` — 100% Local On-Device Storage)
  - `[x]` **Open Source Licenses Screen** (`OpenSourceLicensesScreen`)
  - `[x]` Profile Persistence (DataStore) & Initials Avatar / Profile Picture Fallback
- `[x]` **App Settings & Preferences**
  - `[x]` Master Notifications Toggle
  - `[x]` Selective Notification Categories Bottom Sheet
  - `[x]` Class Reminder Duration Configuration (Presets + Custom Dialog)
  - `[x]` Custom Theme Color Picker Bottom Sheet
  - `[x]` App Version Secret Easter Egg (7-tap trigger)
- `[ ]` **Proper Application Icons & Branding** *(Planned)*: Custom high-resolution adaptive launcher icons, complete app icon suite, and thematic app iconography.
- `[ ]` **Expanded Interactive Popups & Dialogs** *(Planned)*: Additional quick-edit popups, deletion confirmation prompts, detail modals, and contextual action popups across screens.

### 🔔 Notifications Engine
- `[x]` **Academic Reminder Alarm Scheduler & Broadcast Receiver**
- `[x]` **Class Reminders, Assignment Due, Exam Alerts, and Project Deadline Notifications**
- `[x]` **Master Notification Suppression Logic**
- `[x]` **High-Priority Material 3 Notification Channel Setup**

### 📊 Attendance Tracking System
- `[x]` **Dynamic Unit-Based Attendance Engine** (Calculates absence allowances dynamically for 1-unit, 2-unit, 3-unit courses)
- `[x]` **Live Allowance & Warning Badges** (*Safe*, *Warning*, *Critical*, *Advised to Withdraw*)
- `[x]` **Schedule Integration & Automatic Synchronizations**
- `[ ]` **Attendance Tracker Adjustments & Logic Refinements** *(Planned)*: Adjustments to attendance logging workflows, attendance record history editing, manual override controls, and custom threshold rules.

### 📅 Class Schedules & Course Management
- `[x]` **Schedule Display Views**
  - `[x]` Column / Daily Timeline View (`DayScheduleScreen`)
  - `[x]` Full Timetable View (`TimetableGrid` & `WeeklyScheduleScreen`)
  - `[x]` Full Calendar Grid View (`CalendarGridView`)
  - `[x]` Flexible Days Layout (Mon–Fri default + Custom Saturday/Sunday toggles)
- `[x]` **Course Metadata & Modalities**
  - `[x]` Course Code, Name, Section, Units (1–3+ credits), Faculty, Room/Location
  - `[x]` Lecture vs. Laboratory Classification & Modality Tags (Face-to-Face, Asynchronous, Hybrid)
- `[ ]` **Course Schedules Screen Management** *(Planned)*: Full ability to edit existing class schedule blocks, delete schedules, update room/time details, and handle schedule conflicts.

### 📝 Tasks, Assignments, Projects & Exams
- `[x]` **Assignments Tracker** (Deadlines, Due Dates, Status Filtering, Priority Chips)
- `[x]` **Projects Tracker** (Term Project Deadlines, Milestones Checklist, Team Members, Progress Bar)
- `[x]` **Exams Tracker** (Exam Countdown Cards, Venues, Weights %, Study Topic Checklists)
- `[ ]` **Full Academic & Grade Tracker System** *(Planned)*: Comprehensive tracking for course grades, GPA calculations, term targets, and overall academic performance metrics.
- `[ ]` **Dedicated Exams & Quizzes Screen & Features** *(Planned)*: Comprehensive exams & quizzes management screen, score logging, venue/room details, and quiz countdowns.
- `[ ]` **Tasks & Assignments Screen Refinements** *(Planned)*: Sub-task checklists, submission status workflows, priority filtering enhancements, and detailed task views.

### 🔕 Automation & Productivity
- `[x]` **Free Time Period / Gap Checker** (Detecting free gaps between class blocks)
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

## 🛠️ How to Build and Run Lectrium

Lectrium is built using Kotlin and Jetpack Compose. You can easily build and run it on a physical phone or an emulator.

### 📱 Option 1: For Non-Techy Users (Using Android Studio)

If you are not familiar with command-line tools, follow these simple steps:

1. **Install Android Studio**:
   - Download and install the free [Android Studio](https://developer.android.com/studio).
2. **Open the Lectrium Project**:
   - Open Android Studio, click **Open**, and select the `Lectrium` project folder.
   - Wait a moment for Android Studio to download dependencies (you will see a progress bar at the bottom).
3. **Run the App**:
   - Plug your Android phone into your computer via USB (with **USB Debugging** enabled in Developer Options), **OR** launch an Emulator from the Device Manager inside Android Studio.
   - Click the green **Play (▶)** button at the top right of the toolbar.
   - The app will compile and launch on your phone or emulator automatically!

> **Where to find the built APK file?**  
> If you built the app, the `.apk` file ready for manual installation is located at:  
> `app/build/outputs/apk/debug/app-debug.apk`

---

### 💻 Option 2: For Developers (Command Line)

#### Prerequisites
- **JDK 17** or higher installed and configured in your environment.
- **Android SDK** (API 35+) installed.

#### Build Commands

- **Windows (PowerShell or Command Prompt)**:
  ```powershell
  .\gradlew.bat assembleDebug
  ```

- **macOS / Linux (Terminal)**:
  ```bash
  ./gradlew assembleDebug
  ```

#### Install Directly to Connected Device / Emulator
  ```bash
  ./gradlew installDebug
  ```

---

## 🤝 Contributing & Licensing

Released under the **Lectrium Custom Restricted License**.
