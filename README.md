> [!WARNING]
> **PROJECT IN HEAVY DEVELOPMENT**
>
> Please note that **Lectrium** is currently under **HEAVY DEVELOPMENT**. Not all features described in this document have been fully implemented yet. For details on completed, in-progress, and planned features, please refer to the [Feature Implementation Tracker](#-feature-implementation-tracker).

# Lectrium

**Lectrium** is a modern, feature-rich Android application built specifically for **college students** to manage their schedules, class reminders, assignments, exams, attendance tracking, and academic performance.

College academic life differs significantly from Junior/Senior High School systems—with irregular class blocks, varying day schedules, credit units, and unique attendance dynamics. Lectrium is tailored specifically to address these college workflows.

The project takes inspiration from the open-source app [Lectro](https://github.com/Pankaj-Meharchandani/Lectro), extending its core ideas with an expanded feature set, advanced customization, and an improved user interface and user experience (UI/UX) built with modern Jetpack Compose and Material Design 3.

---

## 📍 Feature Implementation Tracker

> **Legend**: `[x]` Implemented | `[/]` In Progress | `[ ]` Planned
> *(Note: This tracker is a living roadmap and is updated continuously as development progresses.)*

### ⚙️ App Core & Architecture
- `[x]` **Jetpack Compose UI Framework & Material 3 Theming**
- `[x]` **Theme & Color Scheme Customization Engine**
  - `[x]` Theme Modes (Follow System / Light Theme / Dark Theme)
  - `[x]` Color Scheme Options (Sky Blue Default, Material You Dynamic Color, Custom Palette)
  - `[x]` Custom ARGB/Hex Color Picker with Live Theme Preview, Preset Swatches, and RGBA Sliders
- `[x]` **Navigation Engine**
  - `[x]` Bottom Navigation Bar Routing
  - `[x]` Navigation Bar Style Customization (Floating Dock with Bouncy Drag Physics vs. Fixed Bottom Bar)
  - `[x]` Smooth Animated Screen & Subscreen Transitions (`AnimatedContent` Fade/Scale)
- `[x]` **User Profile Management**
  - `[x]` **5-Step Interactive Onboarding Walkthrough**:
    - Page 1: Welcome & Scrollable Live Mock Schedule Preview with top-right "Skip" action.
    - Page 2: Student Profile Setup Form (Full Name \*, Email Address \*, Age, Year Level Chips [`1st Year` to `Extendee`], Student Status Chips [`Regular`, `Irregular`, `Shiftee`], Program \*, University \*).
    - Page 3: Class Alert Preferences (Reminder Offset chips, Custom Duration picker, Daily View vs. Weekly Grid preview).
    - Page 4: Attendance Target Setup (Target percentage slider, live subject status badges with Philippine academic grading rules: *Safe*, *Target Met*, *Warning*, *Advised to Withdraw / 5.0*).
    - Page 5: Personalization & Theme Styling.
  - `[x]` **Dedicated Manage Profile Sub-Screen** (`ManageProfileScreen`):
    - Top bar with Back arrow `←` and `"Manage profile"` title.
    - Avatar circle showing user initials (`UserInitials`) or profile photo.
    - System Photo Picker (`PickVisualMedia`) for gallery photo selection & local file storage.
    - Compact form layout with side-by-side fields (`Email address` + `Age`).
    - Year Level & Student Status chips.
    - Save changes action updating DataStore.
  - `[x]` Profile Persistence (DataStore) & Initials Avatar Fallback
- `[x]` **App Settings & Preferences**
  - `[x]` Master Notifications Toggle
  - `[x]` Selective Notification Categories Bottom Sheet
  - `[x]` Class Reminder Duration Configuration (Presets + Custom Dialog with Minutes/Hours/Days units)
  - `[x]` Custom Theme Color Picker Bottom Sheet
  - `[x]` Manage Profile Navigation Entry with Chevron Indicator (`>`)
  - `[x]` Navigation Bar Style Picker
  - `[x]` App Version Secret Easter Egg (7-tap trigger)

### 🔔 Notifications Engine
- `[x]` **Class Reminder Broadcast Receiver**
- `[x]` **Master Notification Suppression Logic**
- `[x]` **High-Priority Notification Channel Setup**

### 📅 Class Schedules & Course Management
- `[ ]` **Schedule Display Views**
  - `[ ]` Column / Timetable View
  - `[ ]` Full Calendar View
  - `[ ]` Flexible Days Layout (Mon–Fri default + Custom Saturday/Sunday days)
- `[ ]` **Course Metadata**
  - `[ ]` Course Code, Course Name, Course Section
  - `[ ]` Course Units / Credits
  - `[ ]` Course Faculty / Professor Info
  - `[ ]` Course Room & Location
- `[ ]` **Class Classification & Modalities**
  - `[ ]` Lecture vs. Laboratory Classification
  - `[ ]` Delivery Modality Tags (Face-to-Face, Asynchronous, Hybrid)
  - `[ ]` Smart Laboratory Constraints (Restricting Labs to Face-to-Face only)

### 📊 Attendance Tracking
- `[ ]` **Attendance Marking System**
- `[ ]` **Custom Status Options** (e.g., *No Class*, *Professor On Leave*, *Holiday*)
- `[ ]` **Custom Status Color Coding**
- `[ ]` **Attendance Summary & Percentage Statistics**

### 📝 Tasks, Assignments & Exams
- `[ ]` **Assignments Tracker** (Deadlines, Due Dates, Submission Status)
- `[ ]` **Exams Tracker** (Exam Dates, Topics, Room Locations)

### 📈 Academic Performance Tools
- `[ ]` **GPA Calculator** (Semester GPA & Cumulative GPA)
- `[ ]` **Target Grade Estimator** ("What grade do I need on the final exam?")

### 🔕 Automation & Productivity
- `[ ]` **In-Class Auto-Mute / Do Not Disturb (DND)**
- `[ ]` **Free Time Period / Gap Checker** (Detecting gaps between class blocks)
- `[ ]` **Export Schedule as Image**
- `[ ]` **Makeup Class Schedule Overrides** (Temporary schedule shifts)

### 📱 System Integration & Archiving
- `[ ]` **Home Screen Widgets** (Daily schedule at a glance & upcoming deadlines)
- `[ ]` **Academic Archive Manager**
  - `[ ]` Semester Schedule Reset
  - `[ ]` Option to Archive Past Term Records or Delete Permanently

---

## 🌟 Key Features Overview

### 🎨 Deep Customization & Theme Engine
- **Theme Modes**: Easily switch between **System Default**, **Light Theme**, and **Dark Theme**.
- **Color Schemes**: Choose between **Sky Blue (Default)**, **Material You Dynamic Color** (matching Android 12+ wallpaper accents), or **Custom Theme**.
- **Custom RGBA Color Picker**: Fine-tune custom primary colors using RGBA sliders, hex input field (`#HEX`), preset swatches, and a live preview card.
- **Navigation Dock Style**: Toggle between a classic **Fixed Bottom Bar** and a floating **Interactive Dock** with bouncy spring drag gesture physics.

### 🚀 5-Step Interactive Onboarding
- **Page 1 (Welcome & Schedule)**: Features a top-right **"Skip"** button, key benefit cards, and a scrollable **Live Schedule Preview** showing a realistic daily timetable.
- **Page 2 (Student Profile Setup)**: Collects student identity details (**Full Name**, **Email**, **Age**, **Year Level**, **Student Status**, **Program**, **University**). Required fields prevent skipping without key information.
- **Page 3 (Class Alerts)**: Configures default reminder offsets (`5 min` to `30 min` + `Custom...`) and displays live daily notification / weekly grid previews.
- **Page 4 (Attendance Targets)**: Interactive target percentage slider (`60%` to `95%`) with live subject status cards (*Safe*, *Target Met*, *Warning*, *Advised to Withdraw / 5.0*).
- **Page 5 (Personalization)**: Fine-tune app theme mode, color scheme palette, and navigation dock style before entering the app.

### 👤 Manage Profile Sub-Screen
- Dedicated subscreen accessible via Settings featuring a `>` chevron indicator.
- Tap avatar to upload a profile picture from gallery (`PickVisualMedia`) or view initials avatar fallback.
- Space-efficient side-by-side layout for compact fields (`Email address` + `Age`) and filter chips for Year Level (`1st year` to `5th year+`) & Student Status (`Regular`, `Irregular`, `Shiftee`).
- Saves changes directly to DataStore preferences.

---

## 🔥 Exclusive Lectrium Features (Not in Lectro)

- 🎨 **RGBA & Hex Custom Theme Engine**: Custom color picker and Material 3 dynamic color generation.
- ⛵ **Floating Dock Navigation**: Bouncy spring-animated floating navigation dock alongside standard bottom bar.
- 📊 **Philippine / Academic Attendance Status Rules**: Attendance targets warning when absences exceed the 20% limit (*Advised to Withdraw / 5.0*).
- ⏳ **Free Time Period / Gap Checker**: Automatically detects and highlights free gaps between class blocks during the day.
- 🖼️ **Export Schedule as Image**: Export your class timetable into a high-resolution image to save as a wallpaper or share with classmates.
- 📈 **GPA & Target Grade Calculator**: Integrated grade calculation and target score estimator.

---

## 🤖 Development & Generative AI Acknowledgement

Lectrium was developed through a hybrid engineering approach:
- **Generative AI Assistance**: Leveraged for architectural guidance, boilerplate generation, and debugging assistance.
- **Handcoded & Manual Implementation**: Features, business logic, UI layouts, and state management were handcoded, customized, and refined specifically for Lectrium.
- **External Inspiration**: Inspired by and referenced from existing projects like [Lectro](https://github.com/Pankaj-Meharchandani/Lectro) and modern Android Jetpack best practices.

---

## 🤝 Contributing & Licensing

Contributions, feature suggestions, and bug reports are welcome!

### License Overview
This project is released under the **Lectrium Custom Restricted License**. Under this license:
- ✅ You **CAN** contribute to this repository, submit pull requests, open issues, and suggest improvements.
- ✅ You **CAN** reference code snippets and patterns from this project for educational and learning purposes.
- ❌ You **CANNOT** claim this project, codebase, or branding as your own.
- ❌ You **CANNOT** fork or copy this project to re-distribute, rebrand, or publish it as a separate product or app store listing.

For full license details, please refer to the [LICENSE](./LICENSE) file.
