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

## 🛠️ Building & Running Lectrium

Whether you are a software developer or someone without a technical background, building Lectrium on your computer is straightforward. Follow the instructions below for your preferred method.

### 📋 Prerequisites
Before building, ensure you have:
- **Operating System**: Windows 10/11, macOS, or Linux.
- **Java Development Kit (JDK)**: Java 17 or higher (automatically included if you install Android Studio).
- **Git** (optional): For cloning the repository from the terminal.

---

### 🟢 Method 1: Using Android Studio (Recommended for Everyone)

This is the easiest and most user-friendly method. Android Studio handles downloading necessary components automatically.

#### Step 1: Download & Install Android Studio
1. Download **[Android Studio](https://developer.android.com/studio)** (Ladybug or newer).
2. Run the installer and follow the setup wizard (use default settings).

#### Step 2: Get the Source Code
* **Option A (Download ZIP)**:
  1. On the [Lectrium GitHub repository](https://github.com/corintheknown/Lectrium), click the green **Code** button and select **Download ZIP**.
  2. Extract the downloaded ZIP file to a folder on your computer.
* **Option B (Git Clone)**:
  1. Open your terminal / command prompt and run:
     ```bash
     git clone https://github.com/corintheknown/Lectrium.git
     ```

#### Step 3: Open the Project in Android Studio
1. Launch Android Studio.
2. Click **Open** (or `File > Open`).
3. Select the extracted `Lectrium` folder and click **OK**.
4. Android Studio will start **Gradle Sync** to download dependencies. Wait a minute or two until the progress bar at the bottom completes.

#### Step 4: Run the Application
1. Connect your Android device via USB (ensure **USB Debugging** is enabled in your phone's *Developer Options*), **OR** open **Device Manager** in Android Studio to create a virtual device (emulator).
2. Click the green **Run ▶** button in the top-right toolbar.
3. Android Studio will compile the app and launch it on your connected device or emulator!

---

### 💻 Method 2: Using the Command Line (For Developers / Terminal Users)

If you prefer building directly from the command line without opening Android Studio, use the included Gradle Wrapper.

#### Step 1: Clone the Repository
```bash
git clone https://github.com/corintheknown/Lectrium.git
cd Lectrium
```

#### Step 2: Build the Debug APK

* **On Windows (PowerShell / Command Prompt)**:
  ```powershell
  .\gradlew.bat assembleDebug
  ```

* **On macOS / Linux**:
  ```bash
  chmod +x gradlew
  ./gradlew assembleDebug
  ```

#### Step 3: Locate & Install the APK
* Once the build completes, the generated APK file will be located at:
  ```
  app/build/outputs/apk/debug/app-debug.apk
  ```
* You can transfer this APK file to your Android phone or install it directly via ADB:
  ```bash
  adb install app/build/outputs/apk/debug/app-debug.apk
  ```

---

### ❓ Troubleshooting Common Build Issues

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| **Gradle Sync Failed** | Missing internet connection or interrupted download | Ensure you are connected to the internet and click **Sync Project with Gradle Files** (elephant icon) in the top-right of Android Studio. |
| **Unsupported Java Version** | Older JDK configured in IDE | Go to `Settings > Build, Execution, Deployment > Build Tools > Gradle` and ensure **Gradle JDK** is set to **Java 17** or higher (or *Embedded JDK*). |
| **Device Not Detected** | USB Debugging disabled on phone | Open phone settings > *About Phone* > tap *Build Number* 7 times to enable Developer Options. Go to *Developer Options* and enable **USB Debugging**. |

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
