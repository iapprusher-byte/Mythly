# Mythly - KMP Edition 🪔

**Ancient Hindu Wisdom, Daily**

A Kotlin Multiplatform Mobile (KMP) app delivering daily Hindu mythology stories with beautiful UI, audio narration, and streak tracking.

## 🎯 Project Overview

**Tech Stack:**
- **Platform:** Kotlin Multiplatform Mobile (KMP)
- **UI:** Jetpack Compose (Android) / Compose Multiplatform
- **Architecture:** Clean Architecture (Domain, Data, Presentation)
- **Dependency Injection:** Koin
- **Image Loading:** Coil
- **Local Database:** Room (with KMP support)
- **Backend:** Supabase
- **Target Platforms:** Android (iOS ready with KMP)

## 📁 Project Structure

```
mythly-kmp/
├── composeApp/           # Shared Compose UI
│   ├── src/
│   │   ├── commonMain/   # Shared code
│   │   ├── androidMain/  # Android-specific
│   │   └── iosMain/      # iOS-specific (future)
├── shared/               # Shared business logic
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── domain/   # Business logic
│   │   │   ├── data/     # Data layer
│   │   │   └── di/       # Dependency injection
│   │   ├── androidMain/  # Platform-specific
│   │   └── iosMain/      # Platform-specific
├── androidApp/           # Android entry point
└── iosApp/               # iOS entry point (future)
```

## 🚀 MVP Features (v1.0)

1. ✅ Daily Story Delivery
2. ✅ Beautiful Story Reader
3. ✅ Story Library with Filters
4. ✅ Streak Tracking
5. ✅ Audio Playback (TTS)
6. ✅ Share Functionality
7. ✅ Simple Onboarding

## 📅 Development Timeline

- **Phase 1:** Foundation & Setup (2 weeks)
- **Phase 2:** Core Features & UI (4 weeks)
- **Phase 3:** Data Integration (2 weeks)
- **Phase 4:** Polish & Launch (2 weeks)

**Total:** 10 weeks to MVP

## 🎨 Design System

**Colors:**
- Primary: Saffron (#FF9933)
- Secondary: Sky Blue (#64B5F6)
- Accent: Golden Yellow (#FFC107)

**Typography:**
- Headings: Playfair Display
- Body: Lato

## 📦 Dependencies

See `gradle/libs.versions.toml` for complete dependency list.

## 🏃 Getting Started

1. Clone the repository
2. Open in Android Studio (latest Canary with KMP support)
3. Sync Gradle
4. Run on Android emulator or device

## 📱 Supported Platforms

- ✅ Android (API 24+)
- 🔄 iOS (Future - KMP ready)

## 🔗 Useful Links

- [Development Roadmap](./docs/ROADMAP.md)
- [Architecture Guide](./docs/ARCHITECTURE.md)
- [Phase Plans](./docs/phases/)

---

**Made with ❤️ for Hindu Mythology Enthusiasts**
