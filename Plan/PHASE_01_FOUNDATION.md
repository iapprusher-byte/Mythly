# Phase 1: Foundation & Setup (Weeks 1-2)

## 🎯 Goal
Set up KMP project structure, configure dependencies, implement design system, and create database schema.

## ✅ Deliverables
- [x] KMP project structure initialized
- [x] All dependencies configured
- [x] Design system implemented
- [x] Room database setup
- [x] Koin DI configured
- [x] Dummy JSON data created

---

## 📋 Tasks Breakdown

### Task 1.1: Project Initialization (Day 1-2)

**Goal:** Create KMP project with proper structure

#### Steps:

1. **Create New KMP Project**
   ```bash
   # Use Android Studio KMP Wizard
   # File > New > Project > Kotlin Multiplatform App
   # Name: Mythly
   # Package: com.mythly.app
   # Min SDK: 24
   ```

2. **Configure Project Structure**
   ```
   mythly-kmp/
   ├── androidApp/
   │   └── src/main/
   │       ├── AndroidManifest.xml
   │       └── kotlin/com/mythly/app/
   │           └── MainActivity.kt
   ├── composeApp/
   │   └── src/
   │       ├── commonMain/
   │       │   └── kotlin/
   │       │       ├── App.kt
   │       │       └── presentation/
   │       └── androidMain/
   │           └── kotlin/
   ├── shared/
   │   └── src/
   │       ├── commonMain/
   │       │   └── kotlin/
   │       │       ├── domain/
   │       │       ├── data/
   │       │       └── di/
   │       └── androidMain/
   │           └── kotlin/
   └── gradle/
       └── libs.versions.toml
   ```

3. **Configure gradle/libs.versions.toml**
   ```toml
   [versions]
   kotlin = "1.9.22"
   agp = "8.2.2"
   compose = "1.6.0"
   compose-compiler = "1.5.8"
   compose-multiplatform = "1.6.0"
   koin = "3.5.3"
   room = "2.6.1"
   coil = "3.0.0-alpha06"
   kotlinx-coroutines = "1.8.0"
   kotlinx-serialization = "1.6.2"
   supabase = "2.0.4"
   ktor = "2.3.7"
   datastore = "1.1.0-beta01"
   
   [libraries]
   # Kotlin
   kotlin-test = { module = "org.jetbrains.kotlin:kotlin-test", version.ref = "kotlin" }
   
   # Compose
   compose-ui = { module = "androidx.compose.ui:ui", version.ref = "compose" }
   compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling", version.ref = "compose" }
   compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview", version.ref = "compose" }
   compose-foundation = { module = "androidx.compose.foundation:foundation", version.ref = "compose" }
   compose-material3 = { module = "androidx.compose.material3:material3", version = "1.2.0" }
   compose-navigation = { module = "androidx.navigation:navigation-compose", version = "2.7.6" }
   
   # Lifecycle
   lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version = "2.7.0" }
   lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version = "2.7.0" }
   
   # Koin
   koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
   koin-android = { module = "io.insert-koin:koin-android", version.ref = "koin" }
   koin-compose = { module = "io.insert-koin:koin-androidx-compose", version.ref = "koin" }
   
   # Room
   room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
   room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
   room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
   
   # Coil
   coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
   
   # Coroutines
   kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
   kotlinx-coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "kotlinx-coroutines" }
   
   # Serialization
   kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
   
   # Supabase
   supabase-postgrest = { module = "io.github.jan-tennert.supabase:postgrest-kt", version.ref = "supabase" }
   supabase-storage = { module = "io.github.jan-tennert.supabase:storage-kt", version.ref = "supabase" }
   
   # Ktor (for Supabase)
   ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
   ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
   
   # DataStore
   datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }
   
   [plugins]
   android-application = { id = "com.android.application", version.ref = "agp" }
   android-library = { id = "com.android.library", version.ref = "agp" }
   kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
   kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
   kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
   compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
   ksp = { id = "com.google.devtools.ksp", version = "1.9.22-1.0.17" }
   room = { id = "androidx.room", version.ref = "room" }
   ```

4. **Update shared/build.gradle.kts**
   ```kotlin
   plugins {
       alias(libs.plugins.kotlin.multiplatform)
       alias(libs.plugins.android.library)
       alias(libs.plugins.kotlin.serialization)
       alias(libs.plugins.ksp)
       alias(libs.plugins.room)
   }
   
   kotlin {
       androidTarget()
       
       sourceSets {
           commonMain.dependencies {
               implementation(libs.kotlinx.coroutines.core)
               implementation(libs.kotlinx.serialization.json)
               implementation(libs.koin.core)
               implementation(libs.room.runtime)
               implementation(libs.supabase.postgrest)
               implementation(libs.ktor.client.core)
           }
           
           androidMain.dependencies {
               implementation(libs.kotlinx.coroutines.android)
               implementation(libs.koin.android)
               implementation(libs.room.ktx)
               implementation(libs.ktor.client.android)
               implementation(libs.datastore.preferences)
           }
       }
   }
   
   room {
       schemaDirectory("$projectDir/schemas")
   }
   
   dependencies {
       add("kspAndroid", libs.room.compiler)
   }
   ```

**Verification:**
```bash
./gradlew clean build
# Should compile without errors
```

---

### Task 1.2: Design System Implementation (Day 3-4)

**Goal:** Create complete design system with colors, typography, and theme.

#### 1. Create Color Palette

**File:** `composeApp/src/commonMain/kotlin/presentation/theme/Color.kt`

```kotlin
package presentation.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val SaffronPrimary = Color(0xFFFF9933)
val SaffronLight = Color(0xFFFFB366)
val SaffronDark = Color(0xFFCC7A29)

// Secondary Colors
val SkyBlue = Color(0xFF64B5F6)
val SkyBlueLight = Color(0xFF9BE7FF)
val SkyBlueDark = Color(0xFF2286C3)

// Accent Colors
val GoldenYellow = Color(0xFFFFC107)
val DeepOrange = Color(0xFFE65100)

// Neutrals
val White = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFFAFAFA)
val LightGray = Color(0xFFF5F5F5)
val MediumGray = Color(0xFF9E9E9E)
val DarkGray = Color(0xFF424242)
val Black = Color(0xFF212121)

// Semantic Colors
val StreakFire = Color(0xFFFF5722)
val Success = Color(0xFF4CAF50)
val Error = Color(0xFFF44336)
val Warning = Color(0xFFFFC107)
val Info = Color(0xFF2196F3)

// Background Colors
val BackgroundLight = Color(0xFFFAFAFA)
val BackgroundDark = Color(0xFF121212)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)

// Card Colors
val CardLight = Color(0xFFFFFFFF)
val CardDark = Color(0xFF2C2C2C)
```

#### 2. Create Typography System

**File:** `composeApp/src/commonMain/kotlin/presentation/theme/Typography.kt`

```kotlin
package presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Font families (using system fonts for MVP, add custom later)
val PlayfairDisplay = FontFamily.Serif
val LatoFont = FontFamily.SansSerif

val MythlyTypography = Typography(
    // Display - Large headings
    displayLarge = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // Headlines - Section titles
    headlineLarge = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // Titles - Card titles
    titleLarge = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body - Main content
    bodyLarge = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Labels - Buttons, tabs
    labelLarge = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

#### 3. Create Material Theme

**File:** `composeApp/src/commonMain/kotlin/presentation/theme/Theme.kt`

```kotlin
package presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = White,
    primaryContainer = SaffronLight,
    onPrimaryContainer = Black,
    
    secondary = SkyBlue,
    onSecondary = White,
    secondaryContainer = SkyBlueLight,
    onSecondaryContainer = Black,
    
    tertiary = GoldenYellow,
    onTertiary = Black,
    
    background = BackgroundLight,
    onBackground = Black,
    
    surface = SurfaceLight,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    
    error = Error,
    onError = White,
    
    outline = MediumGray,
    outlineVariant = LightGray
)

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    onPrimary = Black,
    primaryContainer = SaffronDark,
    onPrimaryContainer = White,
    
    secondary = SkyBlue,
    onSecondary = Black,
    secondaryContainer = SkyBlueDark,
    onSecondaryContainer = White,
    
    tertiary = GoldenYellow,
    onTertiary = Black,
    
    background = BackgroundDark,
    onBackground = White,
    
    surface = SurfaceDark,
    onSurface = White,
    surfaceVariant = CardDark,
    onSurfaceVariant = LightGray,
    
    error = Error,
    onError = Black,
    
    outline = MediumGray,
    outlineVariant = DarkGray
)

@Composable
fun MythlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MythlyTypography,
        content = content
    )
}
```

#### 4. Test Theme

**File:** `composeApp/src/commonMain/kotlin/App.kt`

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.theme.MythlyTheme

@Composable
fun App() {
    MythlyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Mythly",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ancient wisdom, daily.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {}) {
                    Text("Get Started")
                }
            }
        }
    }
}
```

**Verification:**
- Run app on emulator
- Check all colors display correctly
- Test dark theme
- Verify typography scales

---

### Task 1.3: Domain Models (Day 5)

**Goal:** Create all domain models and enums.

#### File Structure:
```
shared/src/commonMain/kotlin/domain/model/
├── Story.kt
├── UserStats.kt
├── Deity.kt
├── Epic.kt
├── Value.kt
└── ReadingSession.kt
```

#### 1. Enums

**File:** `shared/src/commonMain/kotlin/domain/model/Deity.kt`

```kotlin
package domain.model

enum class Deity(val displayName: String) {
    KRISHNA("Krishna"),
    RAMA("Rama"),
    SHIVA("Shiva"),
    GANESHA("Ganesha"),
    DURGA("Durga"),
    HANUMAN("Hanuman"),
    VISHNU("Vishnu"),
    BRAHMA("Brahma"),
    LAKSHMI("Lakshmi"),
    SARASWATI("Saraswati"),
    OTHER("Other")
}
```

**File:** `shared/src/commonMain/kotlin/domain/model/Epic.kt`

```kotlin
package domain.model

enum class Epic(val displayName: String) {
    RAMAYANA("Ramayana"),
    MAHABHARATA("Mahabharata"),
    BHAGAVATA_PURANA("Bhagavata Purana"),
    SHIVA_PURANA("Shiva Purana"),
    DEVI_MAHATMYA("Devi Mahatmya"),
    BHAGAVAD_GITA("Bhagavad Gita"),
    FOLKLORE("Folklore"),
    OTHER("Other Puranas")
}
```

**File:** `shared/src/commonMain/kotlin/domain/model/Value.kt`

```kotlin
package domain.model

enum class Value(val displayName: String, val emoji: String) {
    DEVOTION("Devotion", "🙏"),
    COURAGE("Courage", "💪"),
    WISDOM("Wisdom", "🧠"),
    DHARMA("Dharma", "⚖️"),
    COMPASSION("Compassion", "❤️"),
    TRUTH("Truth", "✨"),
    DUTY("Duty", "🎯"),
    HUMILITY("Humility", "🙇"),
    PERSEVERANCE("Perseverance", "🔥"),
    LOYALTY("Loyalty", "🤝")
}
```

#### 2. Core Models

**File:** `shared/src/commonMain/kotlin/domain/model/Story.kt`

```kotlin
package domain.model

import kotlinx.serialization.Serializable

@Serializable
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
    val audioUrl: String? = null,
    val sanskritTitle: String? = null,
    val relatedStoryIds: List<String> = emptyList()
)

// UI-specific wrapper
data class StoryUiState(
    val story: Story,
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val lastReadAt: Long? = null
)
```

**File:** `shared/src/commonMain/kotlin/domain/model/UserStats.kt`

```kotlin
package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalStoriesRead: Int = 0,
    val lastReadDate: Long? = null,
    val favoriteDeity: Deity? = null,
    val readingStreak7Day: Boolean = false,
    val readingStreak30Day: Boolean = false,
    val readingStreak100Day: Boolean = false,
    val totalReadingTimeMinutes: Int = 0
)

@Serializable
data class ReadingSession(
    val id: String,
    val storyId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val readingTimeSeconds: Int = 0,
    val usedAudio: Boolean = false
)
```

**Verification:**
```bash
./gradlew :shared:build
# Should compile without errors
```

---

### Task 1.4: Database Setup (Day 6-7)

**Goal:** Create Room database with entities, DAOs, and migrations.

#### 1. Database Entities

**File:** `shared/src/commonMain/kotlin/data/local/entity/StoryEntity.kt`

```kotlin
package data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import domain.model.Deity
import domain.model.Epic
import domain.model.Value
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "stories")
@TypeConverters(StoryConverters::class)
data class StoryEntity(
    @PrimaryKey
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
    val audioUrl: String? = null,
    val sanskritTitle: String? = null,
    val relatedStoryIds: List<String> = emptyList(),
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val lastReadAt: Long? = null
)

class StoryConverters {
    @TypeConverter
    fun fromValuesList(values: List<Value>): String {
        return Json.encodeToString(values)
    }
    
    @TypeConverter
    fun toValuesList(valuesString: String): List<Value> {
        return Json.decodeFromString(valuesString)
    }
    
    @TypeConverter
    fun fromStringList(strings: List<String>): String {
        return Json.encodeToString(strings)
    }
    
    @TypeConverter
    fun toStringList(stringsString: String): List<String> {
        return Json.decodeFromString(stringsString)
    }
}
```

**File:** `shared/src/commonMain/kotlin/data/local/entity/UserStatsEntity.kt`

```kotlin
package data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.model.Deity

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Int = 1, // Single row
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalStoriesRead: Int = 0,
    val lastReadDate: Long? = null,
    val favoriteDeity: Deity? = null,
    val readingStreak7Day: Boolean = false,
    val readingStreak30Day: Boolean = false,
    val readingStreak100Day: Boolean = false,
    val totalReadingTimeMinutes: Int = 0
)
```

**File:** `shared/src/commonMain/kotlin/data/local/entity/ReadingSessionEntity.kt`

```kotlin
package data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_sessions")
data class ReadingSessionEntity(
    @PrimaryKey
    val id: String,
    val storyId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val readingTimeSeconds: Int = 0,
    val usedAudio: Boolean = false
)
```

#### 2. DAOs

**File:** `shared/src/commonMain/kotlin/data/local/dao/StoryDao.kt`

```kotlin
package data.local.dao

import androidx.room.*
import data.local.entity.StoryEntity
import domain.model.Deity
import domain.model.Epic
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY datePublished DESC")
    fun getAllStories(): Flow<List<StoryEntity>>
    
    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStoryById(id: String): StoryEntity?
    
    @Query("SELECT * FROM stories WHERE deity = :deity ORDER BY datePublished DESC")
    fun getStoriesByDeity(deity: Deity): Flow<List<StoryEntity>>
    
    @Query("SELECT * FROM stories WHERE epic = :epic ORDER BY datePublished DESC")
    fun getStoriesByEpic(epic: Epic): Flow<List<StoryEntity>>
    
    @Query("""
        SELECT * FROM stories 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%'
        ORDER BY datePublished DESC
    """)
    fun searchStories(query: String): Flow<List<StoryEntity>>
    
    @Query("SELECT * FROM stories WHERE isRead = 1 ORDER BY lastReadAt DESC")
    fun getReadStories(): Flow<List<StoryEntity>>
    
    @Query("SELECT * FROM stories WHERE isFavorite = 1 ORDER BY datePublished DESC")
    fun getFavoriteStories(): Flow<List<StoryEntity>>
    
    @Query("SELECT * FROM stories ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomStory(): StoryEntity?
    
    @Query("SELECT COUNT(*) FROM stories WHERE isRead = 1")
    suspend fun getReadCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)
    
    @Update
    suspend fun updateStory(story: StoryEntity)
    
    @Query("UPDATE stories SET isRead = :isRead, lastReadAt = :timestamp WHERE id = :id")
    suspend fun markAsRead(id: String, isRead: Boolean, timestamp: Long)
    
    @Query("UPDATE stories SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun markAsFavorite(id: String, isFavorite: Boolean)
    
    @Delete
    suspend fun deleteStory(story: StoryEntity)
    
    @Query("DELETE FROM stories")
    suspend fun deleteAllStories()
}
```

**File:** `shared/src/commonMain/kotlin/data/local/dao/UserStatsDao.kt`

```kotlin
package data.local.dao

import androidx.room.*
import data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStatsEntity?>
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStatsEntity)
    
    @Update
    suspend fun updateUserStats(stats: UserStatsEntity)
    
    @Query("UPDATE user_stats SET currentStreak = :streak WHERE id = 1")
    suspend fun updateStreak(streak: Int)
    
    @Query("UPDATE user_stats SET totalStoriesRead = totalStoriesRead + 1 WHERE id = 1")
    suspend fun incrementStoriesRead()
}
```

**File:** `shared/src/commonMain/kotlin/data/local/dao/ReadingSessionDao.kt`

```kotlin
package data.local.dao

import androidx.room.*
import data.local.entity.ReadingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Query("SELECT * FROM reading_sessions ORDER BY startedAt DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<ReadingSessionEntity>>
    
    @Query("SELECT * FROM reading_sessions WHERE storyId = :storyId ORDER BY startedAt DESC")
    fun getSessionsForStory(storyId: String): Flow<List<ReadingSessionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSessionEntity)
    
    @Update
    suspend fun updateSession(session: ReadingSessionEntity)
}
```

#### 3. Database Class

**File:** `shared/src/commonMain/kotlin/data/local/MythlyDatabase.kt`

```kotlin
package data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import data.local.dao.ReadingSessionDao
import data.local.dao.StoryDao
import data.local.dao.UserStatsDao
import data.local.entity.ReadingSessionEntity
import data.local.entity.StoryConverters
import data.local.entity.StoryEntity
import data.local.entity.UserStatsEntity

@Database(
    entities = [
        StoryEntity::class,
        UserStatsEntity::class,
        ReadingSessionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(StoryConverters::class)
abstract class MythlyDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun readingSessionDao(): ReadingSessionDao
}
```

#### 4. Platform-Specific Database Creation

**File:** `shared/src/androidMain/kotlin/data/local/DatabaseFactory.android.kt`

```kotlin
package data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MythlyDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("mythly.db")
    return Room.databaseBuilder(
        context = appContext,
        klass = MythlyDatabase::class.java,
        name = dbFile.absolutePath
    )
}
```

**File:** `shared/src/commonMain/kotlin/data/local/DatabaseFactory.kt`

```kotlin
package data.local

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<MythlyDatabase>
```

**Verification:**
```bash
./gradlew :shared:kspDebugKotlinAndroid
# Check that Room generates code without errors
```

---

### Task 1.5: Dummy JSON Data (Day 8)

**Goal:** Create 10 dummy stories for initial testing.

**File:** `composeApp/src/commonMain/composeResources/files/dummy_stories.json`

```json
{
  "stories": [
    {
      "id": "krishna_001",
      "title": "Krishna and the Butter Thief",
      "content": "Young Krishna was known throughout Vrindavan for his mischievous nature. One day, Mother Yashoda caught him stealing butter from the pot hanging from the ceiling.\n\nWhen questioned, Krishna's innocent smile and butter-smeared face melted her heart. Instead of punishment, she embraced him lovingly.\n\nThe divine child's playfulness taught everyone that God is not distant or fearsome, but close, loving, and accessible to all who approach with pure hearts.\n\nThis simple act of stealing butter represents the soul's yearning for divine sweetness, and God's willingness to be caught by those who seek Him with love.",
      "moralLesson": "Divine love is playful and accessible. God delights in being close to those who seek Him with pure, childlike devotion.",
      "deity": "KRISHNA",
      "epic": "BHAGAVATA_PURANA",
      "values": ["DEVOTION", "COMPASSION"],
      "imageUrl": "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704067200000,
      "sanskritTitle": "माखन चोर कृष्ण"
    },
    {
      "id": "rama_001",
      "title": "Rama's Promise to Jatayu",
      "content": "As Rama searched for Sita in the forest, he found the noble vulture Jatayu mortally wounded. The brave bird had fought Ravana while trying to protect Sita.\n\nWith his last breath, Jatayu told Rama the direction Ravana had taken Sita. Rama held the dying bird tenderly, tears flowing from his eyes.\n\nHe performed the final rites for Jatayu himself, honoring him as he would his own father. This act showed that nobility and dharma transcend all boundaries of birth and species.\n\nJatayu's sacrifice and Rama's compassion teach us that true dharma recognizes the nobility of heart, not the accident of birth.",
      "moralLesson": "True nobility lies in righteous action, not birth. Honor and respect should be given to those who uphold dharma, regardless of their form.",
      "deity": "RAMA",
      "epic": "RAMAYANA",
      "values": ["COMPASSION", "DUTY", "LOYALTY"],
      "imageUrl": "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704153600000
    },
    {
      "id": "ganesha_001",
      "title": "How Ganesha Got His Wisdom",
      "content": "Once, the gods decided to choose the wisest among them. Shiva proposed a challenge: whoever could circle the universe three times first would be declared the wisest.\n\nGods mounted their vahanas and raced across the cosmos. But Ganesha, sitting calmly on his mouse, simply circled his parents Shiva and Parvati three times.\n\nWhen questioned, Ganesha replied, 'My parents are my entire universe. For me, there is nothing beyond them.'\n\nImpressed by his wisdom and devotion, the gods declared Ganesha the winner. He understood that true knowledge comes from recognizing what truly matters.",
      "moralLesson": "True wisdom lies in understanding what truly matters. The greatest knowledge is recognizing the divine in what is close to us, not in seeking far and wide.",
      "deity": "GANESHA",
      "epic": "FOLKLORE",
      "values": ["WISDOM", "DEVOTION", "HUMILITY"],
      "imageUrl": "https://images.unsplash.com/photo-1587582423116-ec07293f0395?w=800",
      "readTimeMinutes": 3,
      "datePublished": 1704240000000
    },
    {
      "id": "hanuman_001",
      "title": "Hanuman's Leap of Faith",
      "content": "Standing on the shore of the vast ocean, Hanuman gazed at distant Lanka where Sita was imprisoned. The other vanaras doubted whether anyone could cross the mighty waters.\n\nJambavan reminded Hanuman of his forgotten powers and divine strength. As memories of his abilities returned, Hanuman grew in size and confidence.\n\nWith a mighty roar of 'Jai Shri Ram!', he leaped across the ocean in a single bound, his faith in Rama giving him infinite strength.\n\nThis leap symbolizes the power of self-belief combined with devotion. When we remember our true nature and dedicate our actions to the divine, nothing is impossible.",
      "moralLesson": "When faith and self-belief unite, the impossible becomes possible. Remember your inner strength and dedicate your actions to a higher purpose.",
      "deity": "HANUMAN",
      "epic": "RAMAYANA",
      "values": ["COURAGE", "DEVOTION", "PERSEVERANCE"],
      "imageUrl": "https://images.unsplash.com/photo-1548690596-1c13f5f51c3c?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704326400000
    },
    {
      "id": "shiva_001",
      "title": "Shiva's Dance of Transformation",
      "content": "The gods were troubled by Tripurasura, a demon who had become invincible through boons. They approached Shiva for help.\n\nShiva mounted his chariot, which was crafted from the very elements of the universe. As he released his arrow, he began the cosmic dance - the Tandava.\n\nWith each movement of his dance, creation and destruction moved in perfect harmony. The arrow found its mark, and the demon's fortress crumbled.\n\nBut Shiva continued to dance, reminding everyone that the universe is in constant transformation. What seems like destruction is often necessary for new creation.",
      "moralLesson": "Change is the only constant. Destruction and creation are two sides of the same cosmic dance. Embrace transformation as a natural part of existence.",
      "deity": "SHIVA",
      "epic": "SHIVA_PURANA",
      "values": ["WISDOM", "DHARMA", "COURAGE"],
      "imageUrl": "https://images.unsplash.com/photo-1571897401841-559e42875ff2?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704412800000
    },
    {
      "id": "durga_001",
      "title": "Durga's Victory Over Mahishasura",
      "content": "When the buffalo demon Mahishasura conquered the heavens through his penance and power, the gods were helpless. No male deity could defeat him due to a boon he had received.\n\nIn their desperation, all the gods combined their energies to create Goddess Durga. Each god gave her a weapon - Shiva's trident, Vishnu's discus, Indra's thunderbolt.\n\nFor nine days and nights, Durga battled the shape-shifting demon. Finally, on the tenth day, she slayed Mahishasura, restoring cosmic balance.\n\nThis victory celebrates the triumph of good over evil and the supreme power of the divine feminine - Shakti - the energy that sustains the universe.",
      "moralLesson": "The divine feminine power (Shakti) is the ultimate force in the universe. Good always triumphs over evil, though the battle may be long and fierce.",
      "deity": "DURGA",
      "epic": "DEVI_MAHATMYA",
      "values": ["COURAGE", "DHARMA", "PERSEVERANCE"],
      "imageUrl": "https://images.unsplash.com/photo-1609619385002-8d03e5bb8415?w=800",
      "readTimeMinutes": 5,
      "datePublished": 1704499200000
    },
    {
      "id": "krishna_002",
      "title": "The Lifting of Govardhan Hill",
      "content": "The people of Vrindavan prepared grand offerings for Indra, the god of rain. Young Krishna questioned this tradition, asking why they worshipped a distant god instead of the mountain and cows that directly nourished them.\n\nThe villagers agreed and worshipped Govardhan hill instead. Enraged, Indra sent torrential rains to flood Vrindavan.\n\nSeven-year-old Krishna lifted the entire Govardhan hill on his little finger, creating a massive umbrella for all the villagers and their cattle.\n\nFor seven days and nights, Krishna held the mountain effortlessly, teaching that divine protection comes to those who question blind rituals and choose wisdom over tradition.",
      "moralLesson": "Question traditions that don't serve the greater good. True divinity protects those who act with wisdom and compassion, not those who follow rituals blindly.",
      "deity": "KRISHNA",
      "epic": "BHAGAVATA_PURANA",
      "values": ["WISDOM", "COURAGE", "COMPASSION"],
      "imageUrl": "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704585600000
    },
    {
      "id": "rama_002",
      "title": "Rama and the Squirrel",
      "content": "During the construction of the bridge to Lanka, mighty vanaras carried huge boulders. A tiny squirrel, wanting to help, carried small pebbles and rolled in the sand to fill gaps.\n\nSome vanaras laughed at the squirrel's insignificant contribution. But Rama gently picked up the squirrel and stroked its back lovingly.\n\nThe three lines that appeared on the squirrel's back from Rama's fingers became a blessing for all squirrels thereafter.\n\nRama declared that the squirrel's devotion and effort were as valuable as the vanaras' strength. It's the purity of intention, not the magnitude of action, that matters.",
      "moralLesson": "Every contribution, no matter how small, has value when done with pure devotion. The divine values the spirit of service over the size of the offering.",
      "deity": "RAMA",
      "epic": "RAMAYANA",
      "values": ["HUMILITY", "COMPASSION", "DEVOTION"],
      "imageUrl": "https://images.unsplash.com/photo-1548690596-1c13f5f51c3c?w=800",
      "readTimeMinutes": 3,
      "datePublished": 1704672000000
    },
    {
      "id": "ganesha_002",
      "title": "Ganesha Writes the Mahabharata",
      "content": "When Sage Vyasa was ready to compose the great epic Mahabharata, he needed a scribe who could write at the speed of his thought.\n\nLord Ganesha agreed, but set a condition: Vyasa must dictate without pause. Vyasa countered: Ganesha must understand each verse before writing it.\n\nWhenever Vyasa needed to think, he would compose a complex verse, giving himself time while Ganesha pondered its meaning.\n\nThis collaboration between divine wisdom (Vyasa) and divine intelligence (Ganesha) created one of humanity's greatest treasures, teaching us that knowledge requires both inspiration and understanding.",
      "moralLesson": "True learning requires both listening and understanding. Knowledge gained without comprehension is worthless; take time to understand deeply before moving forward.",
      "deity": "GANESHA",
      "epic": "MAHABHARATA",
      "values": ["WISDOM", "PATIENCE", "HUMILITY"],
      "imageUrl": "https://images.unsplash.com/photo-1587582423116-ec07293f0395?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704758400000
    },
    {
      "id": "vishnu_001",
      "title": "Vishnu's Vamana Avatar",
      "content": "King Bali, through his righteousness and penance, had conquered the three worlds. Though a good king, his power threatened the cosmic balance.\n\nVishnu appeared as Vamana, a dwarf brahmin, asking for just three paces of land as alms. Bali, known for his generosity, immediately agreed.\n\nVamana then expanded to cosmic proportions. With one step he covered the earth, with the second the heavens. For the third step, he asked Bali where to place it.\n\nBali, recognizing the divine, offered his own head. Pleased by his humility and keeping dharma, Vishnu granted him the netherworld to rule and blessed him with eternal devotion.",
      "moralLesson": "True greatness lies in humility, not in power. Even when we lose everything material, surrendering to the divine with grace brings eternal blessings.",
      "deity": "VISHNU",
      "epic": "BHAGAVATA_PURANA",
      "values": ["HUMILITY", "DHARMA", "DEVOTION"],
      "imageUrl": "https://images.unsplash.com/photo-1571897401841-559e42875ff2?w=800",
      "readTimeMinutes": 4,
      "datePublished": 1704844800000
    }
  ]
}
```

**Verification:**
- JSON is valid
- All required fields present
- Dates in correct format
- Image URLs accessible

---

### Task 1.6: Koin DI Setup (Day 9)

**Goal:** Configure dependency injection with Koin.

**File:** `shared/src/commonMain/kotlin/di/AppModule.kt`

```kotlin
package di

import data.local.MythlyDatabase
import data.local.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Database - provided by platform module
    // DAOs
    single { get<MythlyDatabase>().storyDao() }
    single { get<MythlyDatabase>().userStatsDao() }
    single { get<MythlyDatabase>().readingSessionDao() }
}

// Complete modules list
fun getAppModules() = listOf(
    platformModule,
    sharedModule
    // Add more modules as we build them
)
```

**File:** `shared/src/androidMain/kotlin/di/PlatformModule.android.kt`

```kotlin
package di

import data.local.MythlyDatabase
import data.local.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        getDatabaseBuilder(androidContext())
            .fallbackToDestructiveMigration()
            .build()
    }
}
```

**File:** `androidApp/src/main/kotlin/com/mythly/app/MythlyApplication.kt`

```kotlin
package com.mythly.app

import android.app.Application
import di.getAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MythlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@MythlyApplication)
            modules(getAppModules())
        }
    }
}
```

**Update AndroidManifest.xml:**
```xml
<application
    android:name=".MythlyApplication"
    ...>
```

**Verification:**
```bash
./gradlew :androidApp:assembleDebug
# App should build and run
# Check Logcat for Koin initialization
```

---

## ✅ Phase 1 Completion Checklist

- [x] KMP project structure created (using composeApp module only)
- [x] All dependencies added to libs.versions.toml
- [x] Design system implemented (Color, Typography, Theme)
- [x] Theme tested in preview
- [x] Domain models created (Story, UserStats, enums)
- [x] Database entities created
- [x] DAOs implemented
- [x] Database class created
- [x] Platform-specific database factory implemented
- [x] 10 dummy stories JSON created
- [x] Koin DI configured
- [x] Application class setup
- [x] App builds without errors
- [x] Theme displays correctly

**Status:** ✅ COMPLETED
**Build Status:** BUILD SUCCESSFUL
**Date Completed:** November 15, 2024

---

## 📝 Notes

- **Time Estimate:** 2 weeks (10 working days)
- **Dependencies:** None
- **Next Phase:** Phase 2 - Core Features & UI
- **Project Structure Change:** Used `composeApp` module for all code (domain, data, presentation) instead of separate `shared` module for simpler architecture
- **Technology Versions:**
  - Kotlin: 2.1.0
  - KSP: 2.1.0-1.0.29
  - Koin: 4.0.1
  - Room: 2.7.0-alpha12
  - Compose Multiplatform: 1.9.1

## 🎯 Success Criteria

1. App launches successfully
2. Theme displays correctly (light & dark)
3. Database creates without errors
4. Koin provides all dependencies
5. Zero compilation errors
6. Zero runtime crashes

---

**Ready for Phase 2!** 🚀
