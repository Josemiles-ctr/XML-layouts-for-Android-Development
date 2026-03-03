# Mobile Programming XML

An introductory Android application built with **Kotlin** and **XML layouts**, demonstrating the fundamentals of multi-screen Android development — activity navigation, UI layout with `ConstraintLayout`, and the Android back-stack.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Architecture & Key Concepts](#architecture--key-concepts)
- [Dependencies](#dependencies)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Running the App](#running-the-app)
- [Running Tests](#running-tests)
- [Build Variants](#build-variants)

---

## Project Overview

| Property | Value |
|---|---|
| **App Name** | Mobile Programming xml |
| **Package** | `com.example.mobileprogrammingxml` |
| **Language** | Kotlin |
| **UI Approach** | XML Layouts (View system) |
| **Min SDK** | 24 (Android 7.0 Nougat) |
| **Target SDK** | 36 |
| **AGP Version** | 9.0.1 |
| **Theme** | Material3 DayNight (No ActionBar) |

---

## Features

- **Two-screen navigation** — `MainActivity` → `SecondActivity` via an explicit `Intent`.
- **User input** — An `EditText` on the main screen accepts a name from the user.
- **Back-stack management** — The "Go Back" button on `SecondActivity` calls `finish()`, correctly popping the activity off the back stack and returning to `MainActivity`.
- **Edge-to-edge display** — `MainActivity` uses `enableEdgeToEdge()` with `WindowInsetsCompat` so the UI renders behind system bars on modern Android versions.
- **Material3 theming** — Auto light/dark mode via `Theme.Material3.DayNight.NoActionBar`.
- **RTL support** — `android:supportsRtl="true"` is declared in the manifest.

---

## Project Structure

```
MobileProgrammingxml/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mobileprogrammingxml/
│   │   │   │   ├── MainActivity.kt        # Entry point activity
│   │   │   │   └── SecondActivity.kt      # Second screen activity
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml       # Main screen layout
│   │   │   │   │   └── activity_second.xml     # Second screen layout
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml        # String resources
│   │   │   │   │   ├── colors.xml         # Color resources
│   │   │   │   │   └── themes.xml         # App theme (light)
│   │   │   │   ├── values-night/
│   │   │   │   │   └── themes.xml         # App theme (dark)
│   │   │   │   └── xml/
│   │   │   │       ├── backup_rules.xml
│   │   │   │       └── data_extraction_rules.xml
│   │   │   └── AndroidManifest.xml        # App manifest
│   │   ├── test/                          # Unit tests (JUnit)
│   │   └── androidTest/                   # Instrumented tests (Espresso)
│   ├── build.gradle.kts                   # App-level build config
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml                 # Version catalog
├── build.gradle.kts                       # Project-level build config
├── settings.gradle.kts                    # Module inclusion & repo config
├── gradlew / gradlew.bat                  # Gradle wrapper scripts
└── gradle.properties                      # Gradle JVM/project flags
```

---

## Architecture & Key Concepts

### Activities

#### `MainActivity`
- The **launcher activity** (entry point of the app).
- Inflates `activity_main.xml` which contains:
  - An `EditText` for the user to type a name (hint: "Name").
  - A `Button` labelled "Go to Second Activity".
- Calls `enableEdgeToEdge()` to extend the layout behind system bars, then applies padding via `ViewCompat.setOnApplyWindowInsetsListener`.
- The button uses `android:onClick="goToSecondActivity"` which maps to the `goToSecondActivity(view: View)` method — this creates an explicit `Intent` targeting `SecondActivity` and starts it.

#### `SecondActivity`
- Declared in the manifest with `android:parentActivityName=".MainActivity"` to support the system Up button.
- Inflates `activity_second.xml` which contains:
  - A `TextView` (currently showing the "Name" hint as placeholder).
  - A `Button` labelled "Go Back".
- The "Go Back" button uses `android:onClick="goBack"` → `goBack(view)` calls `finish()`, destroying `SecondActivity` and returning to `MainActivity`.

### Navigation Flow

```
App Launch
    │
    ▼
MainActivity (EditText + "Go to Second Activity" button)
    │  startActivity(Intent → SecondActivity)
    ▼
SecondActivity (TextView + "Go Back" button)
    │  finish()
    ▼
MainActivity (resumed)
```

### Layout System

Both screens use **`ConstraintLayout`** — a flexible layout that positions views relative to each other or the parent using constraints, allowing flat (non-nested) view hierarchies which are optimal for performance.

### Resource Management

All user-visible strings are stored in `res/values/strings.xml` (no hard-coded strings in layouts), following Android best practices for localisation.

---

## Dependencies

All versions are managed via the **Gradle Version Catalog** (`gradle/libs.versions.toml`).

| Dependency | Version | Purpose |
|---|---|---|
| `androidx.core:core-ktx` | 1.17.0 | Kotlin extensions for Android core APIs |
| `androidx.appcompat:appcompat` | 1.6.1 | Backwards-compatible Activity/Fragment support |
| `com.google.android.material:material` | 1.10.0 | Material Design 3 components & theming |
| `androidx.activity:activity` | 1.12.4 | `ComponentActivity`, `enableEdgeToEdge()` |
| `androidx.constraintlayout:constraintlayout` | 2.1.4 | Constraint-based layout manager |
| `junit:junit` *(test)* | 4.13.2 | Unit testing framework |
| `androidx.test.ext:junit` *(androidTest)* | 1.3.0 | AndroidX JUnit extensions for instrumented tests |
| `androidx.test.espresso:espresso-core` *(androidTest)* | 3.7.0 | UI testing framework |

---

## Prerequisites

Before setting up the project, ensure you have:

1. **Android Studio** — Ladybug (2024.2) or newer recommended.
   - Download: https://developer.android.com/studio
2. **JDK 11+** — Bundled with Android Studio; no separate install needed.
3. **Android SDK** with the following components (installed via SDK Manager):
   - **SDK Platform**: Android 14 (API 34) or higher (API 36 preferred).
   - **Android SDK Build-Tools**: Latest version.
   - **Android Emulator** (optional, for running without a physical device).
4. **Git** — For cloning the repository.

---

## Setup & Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd MobileProgrammingxml
```

### 2. Open in Android Studio

1. Launch **Android Studio**.
2. Click **"Open"** (or **File → Open**).
3. Navigate to and select the `MobileProgrammingxml` folder.
4. Click **OK** and wait for the project to sync.

### 3. Sync Gradle

Android Studio will automatically trigger a Gradle sync on first open. If it doesn't:
- Click **File → Sync Project with Gradle Files**, or
- Click the 🐘 **Sync Now** banner that appears at the top of the editor.

This downloads all dependencies listed in `libs.versions.toml`.

### 4. Configure SDK Path (if prompted)

If Android Studio reports a missing SDK:
1. Go to **File → Project Structure → SDK Location**.
2. Set the **Android SDK location** to your local SDK path (e.g., `C:\Users\<you>\AppData\Local\Android\Sdk` on Windows).

---

## Running the App

### On a Physical Device

1. Enable **Developer Options** on your Android device:
   - Go to **Settings → About Phone** and tap **Build Number** 7 times.
2. Enable **USB Debugging** in **Developer Options**.
3. Connect your device via USB.
4. In Android Studio, select your device from the device dropdown.
5. Click **▶ Run** (Shift+F10).

### On an Emulator

1. Open **Device Manager** (AVD Manager) in Android Studio.
2. Click **Create Device**, choose a hardware profile (e.g., Pixel 6), and select a system image (API 24 or higher).
3. Click **Finish**, then launch the emulator.
4. Select the emulator from the device dropdown and click **▶ Run**.

### Via Command Line

```bash
# Debug build and install on connected device/emulator
.\gradlew installDebug

# Then launch manually on the device, or use:
adb shell am start -n com.example.mobileprogrammingxml/.MainActivity
```

---

## Running Tests

### Unit Tests

```bash
.\gradlew test
```

Results are saved to `app/build/reports/tests/testDebugUnitTest/index.html`.

### Instrumented Tests (requires device/emulator)

```bash
.\gradlew connectedAndroidTest
```

Results are saved to `app/build/reports/androidTests/connected/index.html`.

---

## Build Variants

| Variant | Description |
|---|---|
| **debug** | Development build with debugging enabled. Default when using Run in Android Studio. |
| **release** | Optimised build. ProGuard is currently **disabled** (`isMinifyEnabled = false`). Sign with a keystore before distributing. |

To build a release APK:

```bash
.\gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release-unsigned.apk`
