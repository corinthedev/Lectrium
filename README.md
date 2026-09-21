# Lectrium

> [!WARNING]
> **⚠️ CURRENTLY UNDER ACTIVE REVAMP & DEVELOPMENT**  
> Lectrium is an ongoing project currently undergoing a **complete codebase rewrite and architectural revamp**. The application code is being reconstructed from the ground up to ensure modern Jetpack Compose best practices, highly readable code, optimized performance, and clean architecture. Please refer to the [Feature Implementation Tracker](#-feature-implementation-tracker) below for the current implementation status and roadmap.

---

## 📖 About Lectrium

**Lectrium** is a modern, feature-rich Android application built specifically for **college students** to manage their schedules, class reminders, assignments, projects, exams, attendance tracking, and academic performance.

College academic life differs significantly from Junior/Senior High School systems—with irregular class blocks, varying day schedules, credit units, and unique attendance dynamics. Lectrium is tailored specifically to address these college workflows with an expanded feature set, advanced customization, and an improved user interface and user experience (UI/UX) built with modern Jetpack Compose and Material Design 3.

### 🤖 Generative AI Creation
This application was created utilizing **Generative AI** as an integral part of its development, architecture, planning, and creation process.

### 🔗 Source & Base Inspiration
The project takes inspiration from the open-source app [Lectro](https://github.com/Pankaj-Meharchandani/Lectro), serving as the foundational source and base concept, extending its ideas into a modern Jetpack Compose application.

---

## 📍 Feature Implementation Tracker

> [!NOTE]
> **Legend**: `[x]` Implemented | `[/]` In Progress | `[ ]` Planned / Under Development

### ⚙️ App Core, Settings & System Customization
- `[x]` **Master Notifications Toggle** (Global enable/disable for all app alarms)
- `[x]` **Selective Notification Categories** (Per-category granular controls for class, assignment, exam, and project alerts)
- `[x]` **Class Reminder Lead Time Configuration** (Presets + custom duration dialog)
- `[x]` **Manage Profile Sub-Screen** (Given, Middle, Last Name with local DataStore persistence & initials avatar fallback)
- `[x]` **App Theme Engine** (Follow System / Light Theme / Dark Theme)
- `[x]` **Color Scheme Engine** (Default Sky Blue, Material You Dynamic Colors, Custom ARGB/Hex picker with RGBA sliders & live preview)
- `[x]` **Privacy Policy Screen** (Highlighting 100% local, on-device data storage with zero cloud telemetry)
- `[x]` **Open Source Licenses Screen** (Attribution for third-party libraries)
- `[x]` **App Version Info & Secret Easter Egg** (7-tap interactive trigger)
- `[ ]` **Dashboard Interface Density** (Compact/Crammed layout vs. Comfortable/Spacious padding)
- `[ ]` **Text Size Scaling** (In-app typography scaling presets)
- `[ ]` **Time Format Selection** (12-Hour AM/PM vs. 24-Hour display)
- `[ ]` **Reduce Motion Mode** (Option to disable fluid bouncy/spring animations)
- `[ ]` **Interface High Contrast Mode** (Enhanced daylight legibility)
- `[ ]` **Colorblind Safe Filters** (Palette adjustments for Protanopia, Deuteranopia, and Tritanopia)
- `[ ]` **App Lock & Security** (PIN code and Biometric fingerprint/face scanning)
- `[ ]` **Local-Mode Only Status Indicator** (Explicit visual badge confirming zero cloud sync)
- `[ ]` **Manual Raw JSON User Data Export & Import**
- `[ ]` **Auto Backup Schedule** (Daily / Weekly / Manual-only)
- `[ ]` **Backup Destination Picker** (Modal trigger for directory selection/sharing)
- `[ ]` **Clear All User Data / Factory Reset Modal**
- `[ ]` **In-App Language Picker** (Independent of system locale)
- `[ ]` **Class Focus Mode (Auto-DND)** (Automated notification silencer during active class hours)

### 📅 Class Schedules & Course Management
- `[x]` **Daily Timeline View** (`DayScheduleScreen`)
- `[x]` **Weekly Timetable Grid View** (`TimetableGrid` & `WeeklyScheduleScreen`)
- `[x]` **Full Calendar Grid View** (`CalendarGridView`)
- `[x]` **Flexible Days Layout** (Mon–Fri default + custom Saturday/Sunday toggles)
- `[x]` **Course Metadata & Modalities** (Code, Name, Section, Units, Faculty, Room, Lecture/Lab, Modality)
- `[ ]` **Schedule Conflict Detector** (Real-time detection and visual alerts for overlapping class blocks)
- `[ ]` **Classroom Location & Building Navigator Notes** (Room numbers, floor maps, building codes)
- `[ ]` **Walking-Time-Aware Reminders** (Distance-adjusted reminder lead times between back-to-back class venues)
- `[ ]` **Schedule Exceptions** (One-off schedule overrides for holidays, cancelled classes, or make-up sessions)
- `[ ]` **Free Period Calculations & Display** (Automated gap detection and free period time badges on schedule screens)

### 📊 Attendance Tracking Engine
- `[x]` **Dynamic Unit-Based Attendance Engine** (Calculates absence allowances for 1-unit, 2-unit, 3-unit courses)
- `[x]` **Live Allowance & Warning Badges** (*Safe*, *Warning*, *Critical*)
- `[ ]` **Absence Limit Warning Threshold Badges** (Explicit visual alerts when exceeding max 20% absence allowance)
- `[ ]` **Customizable Attendance Target Goals** (Target 80%, 90%, or 100% attendance goal)
- `[ ]` **Session-by-Session Present / Absent Logging**
- `[ ]` **Custom Attendance Tags** (*No Class*, *Holiday*, *Suspended*, *Field Work*, *Excused* - automatically rendered on Schedule Screen)
- `[ ]` **Region-Based Auto-Marking Holidays** (Automatic regional holiday lookups)
- `[ ]` **100% On-Device Location Privacy** (Location lookups are strictly local, never transmitted, and zero telemetry)

### 📝 Tasks, Assignments, Projects & Exams
- `[x]` **Assignments & Tasks Tracker** (Deadlines, status filtering, priority chips)
- `[x]` **Projects Tracker** (Milestones checklist, team members, progress bar)
- `[x]` **Exams Tracker** (Countdown cards, venues, study topic checklists)
- `[ ]` **Exact Deadline Date & Time Picker** (For assignments and projects)
- `[ ]` **Assignment & Project Status Tags** (*Overdue*, *Passed / Submitted On Time*, *In Progress*, *Pending*)
- `[ ]` **Interactive Submission & Completion Controls** (Mark as submitted/passed, adjust deadlines)
- `[ ]` **Exam Entry Removal & Deletion Controls**
- `[ ]` **Exam Completion Checkbox** (Mark exams as *Taken / Completed*)
- `[ ]` **Exam Status Tags** (*Upcoming*, *Completed*, *Missed*, *Graded*)

### 📷 Smart Capture, Import & Data Portability
- `[ ]` **Schedule Import via Photo / PDF (OCR)** (Parse printed class schedules from images/PDFs)
- `[ ]` **Natural-Language Quick-Add** (Parse text like *"CS101 Mon 10am Room 302"*)
- `[ ]` **Offline QR-Code Local Device Transfer** (Peer-to-peer offline schedule transfer)
- `[ ]` **Encrypted Local Vault Backups** (Passphrase-protected `.lectrium` backup files)

### 🔔 System Notifications, Modes & Live UI Integration
- `[x]` **Academic Reminder Alarm Scheduler & Broadcast Receivers**
- `[ ]` **Actionable Notification Buttons** (*Snooze 10m*, *Mark Present/Absent*, *View Class Details*)
- `[ ]` **Exam Mode Notification Profile** (Silences non-exam alerts during test windows)
- `[ ]` **Ongoing Active Class Banner Notification** (Live updating lock screen/shade tile with room & remaining time)
- `[ ]` **Glanceable Next-Class Countdown Tile** (Always-On Display & lock screen countdown)
- `[ ]` **Quick Settings Tile & Launcher Shortcuts** (System Focus Mode tile and quick action app shortcuts)

### ✨ Personalization, Customization & Convenience
- `[x]` **Material 3 Home Screen Widgets** (Schedules, Daily Schedule, Exams, Assignments, Projects)
- `[x]` **Academic Archive Manager** (Semester schedule resets)
- `[ ]` **Session-Specific Quick Notes** (Temporary scratchpad notes per class block)
- `[ ]` **Themed / Custom App Launcher Icons**
- `[ ]` **In-App "What's New" Release Changelog Modal**
- `[ ]` **Course Syllabi & Document Attachment Manager**
- `[ ]` **Widget Customization Engine** (Transparency, accent color sync, layout density)
- `[ ]` **Custom Class Alarm Ringtones** (Per-course or per-category alert sounds)

### 💡 Advanced Academic Analytics & Utilities
- `[ ]` **Attendance Habit Analytics** (Consistency streaks and peak absence day/time stats)
- `[ ]` **Weekly Workload Heatmap** (Visual heavy vs. light submission weeks)
- `[ ]` **Subject Classroom Time Breakdown**
- `[ ]` **Built-in Pomodoro Study Timer** (Linked to upcoming exams or projects)
- `[ ]` **Exam Revision Checklists** (Per-subject study topic checklists)
- `[ ]` **100% Offline Local Peer Collaboration** (Find common free blocks via Bluetooth/Wi-Fi Direct/QR)
- `[ ]` **Wear OS Smartwatch Companion App** (Watch tile & complications)

---

## 🛠️ How to Build and Run Lectrium

Lectrium is built using Kotlin and Jetpack Compose. You can easily build and run it on a physical phone or an emulator.

### 📱 Option 1: For Non-Techy Users (Using Android Studio)

1. **Install Android Studio**:
   - Download and install the free [Android Studio](https://developer.android.com/studio).
2. **Open the Lectrium Project**:
   - Open Android Studio, click **Open**, and select the `Lectrium` project folder.
   - Wait a moment for Android Studio to download dependencies.
3. **Run the App**:
   - Plug your Android phone into your computer via USB (with **USB Debugging** enabled), **OR** launch an Emulator from the Device Manager inside Android Studio.
   - Click the green **Play (▶)** button at the top right of the toolbar.

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

## 📄 Licensing

Lectrium is released under the **Lectrium Custom All Rights Reserved (ARR) License**.  
Please refer to the full [`LICENSE`](LICENSE) file for terms and conditions.

Copyright (c) 2026 Corin. All rights reserved.
