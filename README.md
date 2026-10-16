# 🌿 LifeOS — Open-Source AI-Native Life Operating System for Android

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-API%2037%20%28Baklava%29-brightgreen.svg)](https://developer.android.com/about/versions/16)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%20Material%203-green.svg)](https://developer.android.com/jetpack/compose)
[![AppFunctions](https://img.shields.io/badge/AI%20Engine-Jetpack%20AppFunctions%201.0.0--alpha13-purple.svg)](https://developer.android.com/ai/appfunctions)

**LifeOS** is an open-source, native Android personal planning and productivity application built from the ground up for modern Android. It integrates deeply with **Android 16+ Jetpack AppFunctions**, enabling on-device AI assistants (**Google Gemini**, **Claude AI**, **ChatGPT**, and custom AI agents) to query, schedule, and manage your daily plan, tasks, personal notes, habits, and financial bills.

---

## 📸 Screenshots & Showcase



| Main Plan Screen | Tasks & To-Dos |
| :---: | :---: |
| ![LifeOS Plan Screen](./docs/images/hero_overview.png) <br> *(Placeholder: Add a screenshot of the main Plan screen showing weekly timeline, intention banner, and scheduled events)* | ![LifeOS Tasks Screen](./docs/images/tasks_screen.png) <br> *(Placeholder: Add a screenshot of the Tasks screen showing open/done filter chips and task cost calculation)* |

| Money & Financial Tracker | AI Interoperability & Gemini |
| :---: | :---: |
| ![LifeOS Money Screen](./docs/images/money_screen.png) <br> *(Placeholder: Add a screenshot of the Money screen showing monthly budget progress, bill tracker, and expenses)* | ![LifeOS AI Integration](./docs/images/ai_integration.png) <br> *(Placeholder: Add a screenshot or diagram showing Gemini or Claude interacting with LifeOS via AppFunctions)* |

---

## ✨ Features & Functional Specifications

### 🗓️ 1. Daily & Weekly Horizon Planning
- **Daily Timeline**: Hour-by-hour schedule view with real-time conflict detection.
- **Intention Banner**: Set weekly goals and motivational intentions.
- **Category Filtering**: Filter schedule by *Work*, *Personal*, *Health*, *Home*, and *Study*.
- **Multi-View Horizon**: Seamlessly toggle between *Day*, *Week*, and *Month* views.

### ──────── 2. Tasks & Actionables
- **Priority Management**: Priority ranking (*High*, *Medium*, *Low*).
- **Cost Estimation**: Assign estimated costs to tasks for budget forecasting.
- **Filter Chips**: Quickly switch between *Open*, *Done*, and *All* tasks.

### 💰 3. Financial Management & Bills
- **Budget Tracking**: Visual spending progress card (Spent vs. Remaining Budget).
- **Bill Management**: Track recurring utility and subscription bills with due dates.
- **One-Tap Payment**: Mark bills as paid with automatic expense recording.

### 📝 4. Notes, Habits & Personal Vault
- **Personal Notes**: Tagged note-taking with instant search query.
- **Habit Tracker**: Track daily habits with streak counters.
- **Digital Vault**: Secure storage for important records and warranty dates.

---

## 🤖 AI Interoperability Architecture

LifeOS features a **tri-layer AI connection engine** that works seamlessly across all AI assistant providers on Android:

```
┌─────────────────────────────────────────────────────────────────┐
│                  AI Provider Interoperability                   │
├──────────────────────────┬──────────────────────────┬───────────┤
│    Android Gemini AI     │        Claude AI         │  ChatGPT  │
└────────────┬─────────────┴────────────┬─────────────┴─────┬─────┘
             │                          │                   │
             ▼                          ▼                   ▼
    ┌─────────────────┐        ┌─────────────────┐  ┌───────────────┐
    │  AppFunctions   │        │   Deep Links    │  │ System Share  │
    │  (OS Protocol)  │        │   (lifeos://)   │  │ (ACTION_SEND) │
    └────────┬────────┘        └────────┬────────┘  └───────┬───────┘
             │                          │                   │
             └──────────────────────────┼───────────────────┘
                                        ▼
                   ┌──────────────────────────────────┐
                   │    BaseLifeOSAppFunctionService   │
                   │               &                  │
                   │          MainActivity            │
                   └────────────────┬─────────────────┘
                                    ▼
                         ┌──────────────────────┐
                         │   Room Persistence   │
                         │   (LifeOSDatabase)   │
                         └──────────────────────┘
```

### 1. Jetpack AppFunctions (`androidx.appfunctions`)
Exposes structured on-device capabilities to system AI agents without opening the UI:
- `createEvent`: Schedule calendar events and meetings.
- `createTask`: Add new tasks with priority and cost estimates.
- `createNote`: Save personal notes and summaries.
- `createBill`: Add financial bills and due dates.
- `createHabit`: Register new habit trackers.
- `markTaskCompleted`: Toggle task completion state.
- `queryEvents`: Query scheduled calendar events.
- `queryTasks`: Query active or completed tasks.
- `queryNotes`: Search notes by keyword.
- `queryMoneySummary`: Retrieve unpaid bills and financial overview.
- `getLifeOSInsights`: Fetch complete workspace status.

### 2. Universal Deep Links (`lifeos://`)
Deep link URIs for external AI apps (Claude AI, ChatGPT, Tasker, or web browsers):
- `lifeos://task?title=Task+Name&priority=High`
- `lifeos://event?title=Meeting&date=2026-10-16&startTime=09:00`
- `lifeos://note?title=Summary&content=...`

### 3. Android System Share Sheet (`Intent.ACTION_SEND`)
Share text or responses directly from Claude AI, ChatGPT, or Chrome into LifeOS to instantly save them as tasks or notes.

---

## 🛠️ Tech Stack & Architecture

- **Language**: 100% [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 Theme
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern + Clean Architecture
- **Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Asynchrony**: Kotlin Coroutines & `StateFlow`
- **Compiler / Processing**: KSP (Kotlin Symbol Processing) + AppFunctions Compiler

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: Version 17
- **Android SDK**: API Level 37 (compileSdk 37, minSdk 24)

### Building & Installing

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/LifeOS.git
   cd LifeOS
   ```

2. **Build the Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install to your Android Device via USB Debugging**:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📄 License

This project is licensed under the **MIT License**. See the [LICENSE](./LICENSE) file for full details.

```text
MIT License
Copyright (c) 2026 LifeOS Contributors
```
