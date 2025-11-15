# Mythly Architecture Guide

## 🏗️ Architecture Overview

Mythly follows **Clean Architecture** principles with **Kotlin Multiplatform (KMP)** for maximum code sharing and platform flexibility.

## 📊 Architecture Layers

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (Compose Multiplatform - UI)         │
│   - Screens, ViewModels, Components     │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│          Domain Layer                    │
│      (Pure Kotlin - Shared)              │
│   - Models, UseCases, Repositories       │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│           Data Layer                     │
│    (Repository Implementations)          │
│   - Local DB, Remote API, Cache          │
└─────────────────────────────────────────┘
```

## 🗂️ Detailed Structure

### 1. Presentation Layer (`composeApp/src/commonMain`)

**Location:** `composeApp/src/commonMain/kotlin/presentation/`

```
presentation/
├── theme/
│   ├── Color.kt              # App color palette
│   ├── Typography.kt         # Text styles
│   └── Theme.kt              # Material theme
├── navigation/
│   └── NavGraph.kt           # Navigation setup
├── screens/
│   ├── today/
│   │   ├── TodayScreen.kt
│   │   └── TodayViewModel.kt
│   ├── library/
│   │   ├── LibraryScreen.kt
│   │   └── LibraryViewModel.kt
│   ├── reader/
│   │   ├── StoryReaderScreen.kt
│   │   └── StoryReaderViewModel.kt
│   ├── profile/
│   │   ├── ProfileScreen.kt
│   │   └── ProfileViewModel.kt
│   └── onboarding/
│       ├── OnboardingScreen.kt
│       └── OnboardingViewModel.kt
└── components/
    ├── StoryCard.kt          # Reusable story card
    ├── StreakWidget.kt       # Streak display
    ├── FilterChips.kt        # Category filters
    └── LoadingState.kt       # Loading indicators
```

**Responsibilities:**
- UI rendering with Compose
- User interaction handling
- State management via ViewModels
- Navigation flow

### 2. Domain Layer (`shared/src/commonMain/domain`)

**Location:** `shared/src/commonMain/kotlin/domain/`

```
domain/
├── model/
│   ├── Story.kt              # Story entity
│   ├── UserStats.kt          # User statistics
│   ├── Deity.kt              # Deity enum
│   ├── Epic.kt               # Epic enum
│   └── Value.kt              # Values enum
├── repository/
│   ├── StoryRepository.kt    # Story data operations
│   └── UserRepository.kt     # User data operations
└── usecase/
    ├── GetTodayStoryUseCase.kt
    ├── GetAllStoriesUseCase.kt
    ├── GetStoriesByDeityUseCase.kt
    ├── SearchStoriesUseCase.kt
    ├── MarkStoryReadUseCase.kt
    ├── UpdateStreakUseCase.kt
    └── GetUserStatsUseCase.kt
```

**Key Models:**

```kotlin
// Story.kt
data class Story(
    val id: String,
    val title: String,
    val content: String,
    val moralLesson: String,
    val deity: Deity,
    val epic: Epic,
    val values: List<Value>,
    val imageUrl: String,
    val readTimeMinutes: Int,
    val datePublished: Long,
    val isRead: Boolean = false
)

// UserStats.kt
data class UserStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalStoriesRead: Int,
    val lastReadDate: Long?,
    val favoriteDeity: Deity?
)
```

**Responsibilities:**
- Business logic
- Domain models (platform-agnostic)
- Repository interfaces
- Use cases (single responsibility)

### 3. Data Layer (`shared/src/commonMain/data`)

**Location:** `shared/src/commonMain/kotlin/data/`

```
data/
├── local/
│   ├── database/
│   │   ├── MythlyDatabase.kt
│   │   ├── dao/
│   │   │   ├── StoryDao.kt
│   │   │   └── UserStatsDao.kt
│   │   └── entity/
│   │       ├── StoryEntity.kt
│   │       └── UserStatsEntity.kt
│   ├── preferences/
│   │   └── PreferencesManager.kt
│   └── ContentLoader.kt
├── remote/
│   ├── SupabaseClient.kt
│   ├── api/
│   │   └── StoryApi.kt
│   └── dto/
│       └── StoryDto.kt
└── repository/
    ├── StoryRepositoryImpl.kt
    └── UserRepositoryImpl.kt
```

**Responsibilities:**
- Data persistence (Room)
- Remote API calls (Supabase)
- Data transformation (DTO ↔ Domain models)
- Caching strategy

## 🔄 Data Flow

### Reading a Story (Example)

```
User taps story card
       ↓
StoryReaderViewModel.loadStory(id)
       ↓
GetStoryByIdUseCase.execute(id)
       ↓
StoryRepository.getStoryById(id)
       ↓
┌─────────────────┬─────────────────┐
│  Local DB       │  Supabase       │
│  (Cache)        │  (Source)       │
└─────────────────┴─────────────────┘
       ↓
Transform StoryEntity → Story
       ↓
Emit to ViewModel
       ↓
Update UI State
       ↓
Render StoryReaderScreen
```

## 🧩 Dependency Injection (Koin)

**Location:** `shared/src/commonMain/kotlin/di/`

```kotlin
// AppModule.kt
val appModule = module {
    // Database
    single { createDatabase(get()) }
    single { get<MythlyDatabase>().storyDao() }
    single { get<MythlyDatabase>().userStatsDao() }
    
    // Repositories
    single<StoryRepository> { StoryRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    
    // Use Cases
    factory { GetTodayStoryUseCase(get()) }
    factory { GetAllStoriesUseCase(get()) }
    factory { MarkStoryReadUseCase(get(), get()) }
    factory { UpdateStreakUseCase(get()) }
    
    // ViewModels
    viewModel { TodayViewModel(get(), get()) }
    viewModel { LibraryViewModel(get(), get()) }
    viewModel { (id: String) -> StoryReaderViewModel(id, get(), get()) }
    viewModel { ProfileViewModel(get()) }
}
```

## 🎯 Platform-Specific Code

### Android-Specific (`shared/src/androidMain`)

```kotlin
// Database creation
actual fun createDatabase(context: Any): MythlyDatabase {
    val ctx = context as Context
    return Room.databaseBuilder(
        ctx,
        MythlyDatabase::class.java,
        "mythly.db"
    ).build()
}

// TTS Manager
actual class TTSManager(private val context: Context) {
    private var tts: TextToSpeech? = null
    // Implementation...
}
```

### iOS-Specific (`shared/src/iosMain`) - Future

```kotlin
// Database creation
actual fun createDatabase(context: Any): MythlyDatabase {
    // iOS Room implementation
}

// TTS Manager
actual class TTSManager {
    // iOS AVSpeechSynthesizer implementation
}
```

## 📝 Key Principles

1. **Separation of Concerns**
   - Each layer has a single responsibility
   - No business logic in UI
   - No UI code in domain

2. **Dependency Rule**
   - Dependencies point inward
   - Domain layer has zero dependencies
   - Data layer depends on domain

3. **Platform Agnostic**
   - Maximum shared code in `commonMain`
   - Platform-specific only when necessary
   - Expect/actual for platform APIs

4. **Testability**
   - Use cases are easily testable
   - Repository interfaces for mocking
   - ViewModels test business logic

5. **Scalability**
   - Easy to add new features
   - Easy to add new platforms
   - Easy to swap implementations

## 🧪 Testing Strategy

```
shared/
├── commonTest/
│   ├── domain/
│   │   └── usecase/
│   │       └── GetTodayStoryUseCaseTest.kt
│   └── data/
│       └── repository/
│           └── StoryRepositoryTest.kt
├── androidTest/
│   └── database/
│       └── StoryDaoTest.kt
└── iosTest/
    └── (iOS-specific tests)
```

## 🔧 Build Configuration

**gradle/libs.versions.toml:**
```toml
[versions]
kotlin = "1.9.22"
compose = "1.6.0"
koin = "3.5.3"
room = "2.6.1"
coil = "3.0.0-alpha04"

[libraries]
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
```

## 🚀 Next Steps

1. Set up KMP project structure
2. Implement domain models
3. Create database schema
4. Build repository implementations
5. Develop UI screens

---

For detailed implementation, see phase-specific documentation in `/docs/phases/`.
