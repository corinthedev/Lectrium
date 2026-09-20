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
- `[x]` **Theme Switching System** (System Default / Light Theme / Dark Theme)
- `[x]` **Navigation Engine**
  - `[x]` Bottom Navigation Bar Routing
  - `[x]` Navigation Bar Style Customization (Floating Bar vs. Fixed Bottom Bar)
- `[/]` **User Profile Management**
  - `[/]` Multi-step Onboarding Carousel (Name, University, Program, Year Level, Avatar selection)
  - `[/]` Profile Picture Storage & Initials Avatar Fallback
  - `[/]` Local Profile Persistence (DataStore)
- `[x]` **App Settings & Preferences**
  - `[x]` Master Notifications Toggle
  - `[x]` Selective Notification Categories Bottom Sheet
  - `[x]` Class Reminder Duration Configuration (Presets + Custom Dialog with Minutes/Hours/Days units)
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

### 📅 Class Schedules & Course Information
- **Flexible Week Views**: Default Monday-to-Friday layout with full support for adding custom days (e.g., Saturday or Sunday classes).
- **Multiple Layout Views**: Switch between a structured **Column View** (timetable format) and a full **Calendar View**.
- **Class Type & Delivery Modality Tags**:
  - **Class Classification**: Categorize classes as **Lecture** or **Laboratory**.
  - **Delivery Modalities**: Tag courses as *Face-to-Face*, *Asynchronous*, or *Hybrid / Both*.
  - **Smart Laboratory Constraints**: Setting a course as a **Laboratory** automatically restricts the modality to **Face-to-Face** only (disabling Asynchronous and Hybrid options) to reflect hands-on lab requirements.
- **Comprehensive Course Metadata**: Store essential course details including:
  - Course Code & Course Name
  - Course Units / Credits
  - Course Section
  - Course Faculty (Professor / Instructor handling the course)
  - Course Room & Location

### 📊 Customizable Class Attendance
- **Expanded Status Options**: Go beyond standard *Present*, *Absent*, or *Excused* by creating custom attendance statuses (e.g., *No Class*, *Professor On Leave*, *Holiday*).
- **Custom Color Coding**: Fully customize colors for both default and custom attendance statuses.

### 📝 Assignments & 🎓 Exams
- **Assignments**: Track upcoming deadlines, due dates, submission status, and assignment details.
- **Exams**: Keep track of upcoming exams, exam schedules, topics, and room locations.

### 📈 GPA Calculator & Target Grade Estimator
- **GPA Calculation**: Calculate semester and cumulative GPA based on course units and grade inputs.
- **Target Grade Estimator**: Calculate the exact grade needed on final exams or remaining assessments to reach a desired target final grade.

### ⏰ Smart & Custom Class Reminders
- Set notifications before classes using standard preset times (5m, 10m, 15m, 30m, 1h) or custom durations with unit selection (Minutes, Hours, Days) and configurable maximum limits.

### 🔕 Auto-Mute / Do Not Disturb During Class
- Automatically toggle Do Not Disturb (DND) or silent mode on your phone during active class times to prevent unexpected ringers or interruptions during lectures.

### 📱 Home Screen Widgets
- Android Home Screen Widgets to view upcoming classes, daily schedules, and urgent deadlines at a glance without opening the app.

### 🗂️ Academic Archive & Semester Reset Manager
- **Semester Reset**: Clear the current semester's schedule when starting a new academic term.
- **Flexible Archiving Options**: When clearing a schedule, choose whether to **save the current schedule and records to the Academic Archive** for long-term reference or **permanently delete** without saving.

---

## 🔥 Exclusive Lectrium Features (Not in Lectro)

- ⏳ **Free Time Period / Gap Checker**: Automatically detects and highlights free gaps between class blocks during the day, making it easy to plan study sessions, meals, or rest.
- 🖼️ **Export Schedule as Image**: Export your class timetable into a high-resolution image to save as a wallpaper or share with classmates.
- 🔄 **Makeup Class Schedule Overrides**: Temporarily change or override specific class schedules for makeup sessions, time shifts, or room swaps without altering your regular weekly timetable.
- 📈 **GPA & Target Grade Calculator**: Integrated grade calculation and target score estimator.
- 🧪 **Smart Laboratory Constraints**: Automated rules enforcing Face-to-Face modality for laboratory courses.
- 🔕 **In-Class Do Not Disturb**: Automated silent mode during class hours.
- 🗂️ **Academic Archive Manager**: Archive past term schedules and attendance history or clear without saving.

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