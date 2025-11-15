# Phase 2: Core Features & UI (Weeks 3-6)

## 🎯 Goal
Build all core screens, implement navigation, create repositories, use cases, and ViewModels. Get the app functionally complete.

## ✅ Deliverables
- [ ] Navigation system implemented
- [ ] All 5 main screens built
- [ ] Repository layer complete
- [ ] Use cases implemented
- [ ] ViewModels with state management
- [ ] Story content loaded from JSON
- [ ] Dummy data populated in database

---

## 📋 Week 3-4: Data Layer & Navigation

### Task 2.1: Repository Implementation (Days 10-11)

**Goal:** Create repository implementations with clean interfaces.

#### 1. Repository Interfaces (already in domain from Phase 1)

**File:** `shared/src/commonMain/kotlin/domain/repository/StoryRepository.kt`

```kotlin
package domain.repository

import domain.model.Deity
import domain.model.Epic
import domain.model.Story
import domain.model.StoryUiState
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getAllStories(): Flow<List<StoryUiState>>
    suspend fun getStoryById(id: String): StoryUiState?
    fun getStoriesByDeity(deity: Deity): Flow<List<StoryUiState>>
    fun getStoriesByEpic(epic: Epic): Flow<List<StoryUiState>>
    fun searchStories(query: String): Flow<List<StoryUiState>>
    fun getReadStories(): Flow<List<StoryUiState>>
    fun getFavoriteStories(): Flow<List<StoryUiState>>
    suspend fun getTodayStory(): StoryUiState?
    suspend fun markAsRead(storyId: String)
    suspend fun markAsFavorite(storyId: String, isFavorite: Boolean)
    suspend fun loadInitialContent(): Result<Unit>
}
```

**File:** `shared/src/commonMain/kotlin/domain/repository/UserRepository.kt`

```kotlin
package domain.repository

import domain.model.ReadingSession
import domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun updateStreak()
    suspend fun incrementStoriesRead()
    suspend fun updateReadingTime(minutes: Int)
    suspend fun saveReadingSession(session: ReadingSession)
    fun getRecentSessions(): Flow<List<ReadingSession>>
}
```

#### 2. Repository Implementations

**File:** `shared/src/commonMain/kotlin/data/repository/StoryRepositoryImpl.kt`

```kotlin
package data.repository

import data.local.dao.StoryDao
import data.local.entity.StoryEntity
import domain.model.*
import domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock

class StoryRepositoryImpl(
    private val storyDao: StoryDao,
    private val jsonContent: String // We'll inject this
) : StoryRepository {

    override fun getAllStories(): Flow<List<StoryUiState>> =
        storyDao.getAllStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override suspend fun getStoryById(id: String): StoryUiState? =
        storyDao.getStoryById(id)?.toUiState()

    override fun getStoriesByDeity(deity: Deity): Flow<List<StoryUiState>> =
        storyDao.getStoriesByDeity(deity).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getStoriesByEpic(epic: Epic): Flow<List<StoryUiState>> =
        storyDao.getStoriesByEpic(epic).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun searchStories(query: String): Flow<List<StoryUiState>> =
        storyDao.searchStories(query).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getReadStories(): Flow<List<StoryUiState>> =
        storyDao.getReadStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getFavoriteStories(): Flow<List<StoryUiState>> =
        storyDao.getFavoriteStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override suspend fun getTodayStory(): StoryUiState? {
        // Simple algorithm: cycle through stories based on day of year
        val allStories = storyDao.getAllStories()
        // For now, just return a random story
        // In production, use day-based algorithm
        return storyDao.getRandomStory()?.toUiState()
    }

    override suspend fun markAsRead(storyId: String) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        storyDao.markAsRead(storyId, true, timestamp)
    }

    override suspend fun markAsFavorite(storyId: String, isFavorite: Boolean) {
        storyDao.markAsFavorite(storyId, isFavorite)
    }

    override suspend fun loadInitialContent(): Result<Unit> = try {
        // Check if already loaded
        val existingCount = storyDao.getReadCount()
        if (existingCount > 0) {
            return Result.success(Unit)
        }

        // Parse JSON
        val storiesData = Json.decodeFromString<StoriesContainer>(jsonContent)
        
        // Convert to entities
        val entities = storiesData.stories.map { dto ->
            StoryEntity(
                id = dto.id,
                title = dto.title,
                content = dto.content,
                moralLesson = dto.moralLesson,
                deity = dto.deity,
                epic = dto.epic,
                values = dto.values,
                imageUrl = dto.imageUrl,
                readTimeMinutes = dto.readTimeMinutes,
                datePublished = dto.datePublished,
                audioUrl = dto.audioUrl,
                sanskritTitle = dto.sanskritTitle,
                relatedStoryIds = dto.relatedStoryIds ?: emptyList()
            )
        }

        // Insert into database
        storyDao.insertStories(entities)
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun StoryEntity.toUiState() = StoryUiState(
        story = Story(
            id = id,
            title = title,
            content = content,
            moralLesson = moralLesson,
            deity = deity,
            epic = epic,
            values = values,
            imageUrl = imageUrl,
            readTimeMinutes = readTimeMinutes,
            datePublished = datePublished,
            audioUrl = audioUrl,
            sanskritTitle = sanskritTitle,
            relatedStoryIds = relatedStoryIds
        ),
        isRead = isRead,
        isFavorite = isFavorite,
        lastReadAt = lastReadAt
    )
}

@kotlinx.serialization.Serializable
private data class StoriesContainer(
    val stories: List<StoryDto>
)

@kotlinx.serialization.Serializable
private data class StoryDto(
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
    val relatedStoryIds: List<String>? = null
)
```

**File:** `shared/src/commonMain/kotlin/data/repository/UserRepositoryImpl.kt`

```kotlin
package data.repository

import data.local.dao.ReadingSessionDao
import data.local.dao.UserStatsDao
import data.local.entity.ReadingSessionEntity
import data.local.entity.UserStatsEntity
import domain.model.ReadingSession
import domain.model.UserStats
import domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.math.abs

class UserRepositoryImpl(
    private val userStatsDao: UserStatsDao,
    private val readingSessionDao: ReadingSessionDao
) : UserRepository {

    override fun getUserStats(): Flow<UserStats> =
        userStatsDao.getUserStats().map { entity ->
            entity?.toModel() ?: UserStats()
        }

    override suspend fun updateStreak() {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStatsEntity()
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val oneDayMs = 24 * 60 * 60 * 1000L

        val newStreak = when {
            stats.lastReadDate == null -> 1
            abs(currentTime - stats.lastReadDate) <= oneDayMs -> stats.currentStreak
            abs(currentTime - stats.lastReadDate) <= 2 * oneDayMs -> stats.currentStreak + 1
            else -> 1
        }

        val updatedStats = stats.copy(
            currentStreak = newStreak,
            longestStreak = maxOf(stats.longestStreak, newStreak),
            lastReadDate = currentTime,
            readingStreak7Day = newStreak >= 7,
            readingStreak30Day = newStreak >= 30,
            readingStreak100Day = newStreak >= 100
        )

        userStatsDao.insertUserStats(updatedStats)
    }

    override suspend fun incrementStoriesRead() {
        userStatsDao.incrementStoriesRead()
    }

    override suspend fun updateReadingTime(minutes: Int) {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStatsEntity()
        val updatedStats = stats.copy(
            totalReadingTimeMinutes = stats.totalReadingTimeMinutes + minutes
        )
        userStatsDao.updateUserStats(updatedStats)
    }

    override suspend fun saveReadingSession(session: ReadingSession) {
        val entity = ReadingSessionEntity(
            id = session.id,
            storyId = session.storyId,
            startedAt = session.startedAt,
            completedAt = session.completedAt,
            readingTimeSeconds = session.readingTimeSeconds,
            usedAudio = session.usedAudio
        )
        readingSessionDao.insertSession(entity)
    }

    override fun getRecentSessions(): Flow<List<ReadingSession>> =
        readingSessionDao.getRecentSessions().map { entities ->
            entities.map { it.toModel() }
        }

    private fun UserStatsEntity.toModel() = UserStats(
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        totalStoriesRead = totalStoriesRead,
        lastReadDate = lastReadDate,
        favoriteDeity = favoriteDeity,
        readingStreak7Day = readingStreak7Day,
        readingStreak30Day = readingStreak30Day,
        readingStreak100Day = readingStreak100Day,
        totalReadingTimeMinutes = totalReadingTimeMinutes
    )

    private fun ReadingSessionEntity.toModel() = ReadingSession(
        id = id,
        storyId = storyId,
        startedAt = startedAt,
        completedAt = completedAt,
        readingTimeSeconds = readingTimeSeconds,
        usedAudio = usedAudio
    )
}
```

#### 3. Update DI Module

**File:** `shared/src/commonMain/kotlin/di/DataModule.kt`

```kotlin
package di

import data.repository.StoryRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.repository.StoryRepository
import domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<StoryRepository> { 
        StoryRepositoryImpl(
            storyDao = get(),
            jsonContent = get() // Provided by platform module
        )
    }
    
    single<UserRepository> {
        UserRepositoryImpl(
            userStatsDao = get(),
            readingSessionDao = get()
        )
    }
}
```

**Update:** `shared/src/androidMain/kotlin/di/PlatformModule.android.kt`

```kotlin
package di

import android.content.Context
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
    
    // Provide JSON content
    single {
        androidContext().assets.open("dummy_stories.json")
            .bufferedReader()
            .use { it.readText() }
    }
}
```

---

### Task 2.2: Use Cases (Day 12)

**Goal:** Implement clean, single-purpose use cases.

**File:** `shared/src/commonMain/kotlin/domain/usecase/GetTodayStoryUseCase.kt`

```kotlin
package domain.usecase

import domain.model.StoryUiState
import domain.repository.StoryRepository

class GetTodayStoryUseCase(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke(): Result<StoryUiState> = try {
        val story = storyRepository.getTodayStory()
        if (story != null) {
            Result.success(story)
        } else {
            Result.failure(Exception("No story available"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/GetAllStoriesUseCase.kt`

```kotlin
package domain.usecase

import domain.model.StoryUiState
import domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllStoriesUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(): Flow<List<StoryUiState>> {
        return storyRepository.getAllStories()
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/SearchStoriesUseCase.kt`

```kotlin
package domain.usecase

import domain.model.StoryUiState
import domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class SearchStoriesUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(query: String): Flow<List<StoryUiState>> {
        return storyRepository.searchStories(query)
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/FilterStoriesUseCase.kt`

```kotlin
package domain.usecase

import domain.model.Deity
import domain.model.Epic
import domain.model.StoryUiState
import domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class FilterStoriesByDeityUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(deity: Deity): Flow<List<StoryUiState>> {
        return storyRepository.getStoriesByDeity(deity)
    }
}

class FilterStoriesByEpicUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(epic: Epic): Flow<List<StoryUiState>> {
        return storyRepository.getStoriesByEpic(epic)
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/MarkStoryReadUseCase.kt`

```kotlin
package domain.usecase

import domain.model.ReadingSession
import domain.repository.StoryRepository
import domain.repository.UserRepository
import kotlinx.datetime.Clock
import java.util.UUID

class MarkStoryReadUseCase(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        storyId: String,
        readingTimeSeconds: Int = 0,
        usedAudio: Boolean = false
    ): Result<Unit> = try {
        // Mark story as read
        storyRepository.markAsRead(storyId)
        
        // Update user stats
        userRepository.updateStreak()
        userRepository.incrementStoriesRead()
        
        if (readingTimeSeconds > 0) {
            userRepository.updateReadingTime(readingTimeSeconds / 60)
        }
        
        // Save reading session
        val session = ReadingSession(
            id = UUID.randomUUID().toString(),
            storyId = storyId,
            startedAt = Clock.System.now().toEpochMilliseconds(),
            completedAt = Clock.System.now().toEpochMilliseconds(),
            readingTimeSeconds = readingTimeSeconds,
            usedAudio = usedAudio
        )
        userRepository.saveReadingSession(session)
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/GetUserStatsUseCase.kt`

```kotlin
package domain.usecase

import domain.model.UserStats
import domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserStats> {
        return userRepository.getUserStats()
    }
}
```

**File:** `shared/src/commonMain/kotlin/domain/usecase/LoadInitialContentUseCase.kt`

```kotlin
package domain.usecase

import domain.repository.StoryRepository

class LoadInitialContentUseCase(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return storyRepository.loadInitialContent()
    }
}
```

#### Update DI Module

**File:** `shared/src/commonMain/kotlin/di/DomainModule.kt`

```kotlin
package di

import domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    factory { GetTodayStoryUseCase(get()) }
    factory { GetAllStoriesUseCase(get()) }
    factory { SearchStoriesUseCase(get()) }
    factory { FilterStoriesByDeityUseCase(get()) }
    factory { FilterStoriesByEpicUseCase(get()) }
    factory { MarkStoryReadUseCase(get(), get()) }
    factory { GetUserStatsUseCase(get()) }
    factory { LoadInitialContentUseCase(get()) }
}
```

**Update getAppModules():**

```kotlin
fun getAppModules() = listOf(
    platformModule,
    sharedModule,
    dataModule,
    domainModule
)
```

---

### Task 2.3: Navigation Setup (Day 13)

**Goal:** Implement type-safe navigation with Compose.

**File:** `composeApp/src/commonMain/kotlin/presentation/navigation/Screen.kt`

```kotlin
package presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Today : Screen
    
    @Serializable
    data object Library : Screen
    
    @Serializable
    data object Profile : Screen
    
    @Serializable
    data class StoryReader(val storyId: String) : Screen
    
    @Serializable
    data object Onboarding : Screen
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/navigation/NavGraph.kt`

```kotlin
package presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import presentation.screens.library.LibraryScreen
import presentation.screens.onboarding.OnboardingScreen
import presentation.screens.profile.ProfileScreen
import presentation.screens.reader.StoryReaderScreen
import presentation.screens.today.TodayScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Onboarding> {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Today) {
                        popUpTo<Screen.Onboarding> { inclusive = true }
                    }
                }
            )
        }
        
        composable<Screen.Today> {
            TodayScreen(
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryReader(storyId))
                }
            )
        }
        
        composable<Screen.Library> {
            LibraryScreen(
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryReader(storyId))
                }
            )
        }
        
        composable<Screen.Profile> {
            ProfileScreen()
        }
        
        composable<Screen.StoryReader> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.StoryReader>()
            StoryReaderScreen(
                storyId = args.storyId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/components/BottomNavBar.kt`

```kotlin
package presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import presentation.navigation.Screen

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    data object Today : BottomNavItem(Screen.Today, "Today", Icons.Filled.Home)
    data object Library : BottomNavItem(Screen.Library, "Library", Icons.Filled.List)
    data object Profile : BottomNavItem(Screen.Profile, "Profile", Icons.Filled.Person)
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        BottomNavItem.Today,
        BottomNavItem.Library,
        BottomNavItem.Profile
    )
    
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentScreen == item.screen,
                onClick = { onNavigate(item.screen) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
```

---

## 📋 Week 5-6: Screen Implementation

### Task 2.4: Today Screen (Days 14-15)

**Goal:** Build Today screen with story of the day.

**File:** `composeApp/src/commonMain/kotlin/presentation/screens/today/TodayViewModel.kt`

```kotlin
package presentation.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.StoryUiState
import domain.model.UserStats
import domain.usecase.GetTodayStoryUseCase
import domain.usecase.GetUserStatsUseCase
import domain.usecase.LoadInitialContentUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TodayUiState(
    val isLoading: Boolean = true,
    val todayStory: StoryUiState? = null,
    val userStats: UserStats = UserStats(),
    val error: String? = null,
    val isContentLoaded: Boolean = false
)

class TodayViewModel(
    private val getTodayStoryUseCase: GetTodayStoryUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val loadInitialContentUseCase: LoadInitialContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadInitialContent()
        observeUserStats()
    }

    private fun loadInitialContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            loadInitialContentUseCase()
                .onSuccess {
                    loadTodayStory()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun loadTodayStory() {
        viewModelScope.launch {
            getTodayStoryUseCase()
                .onSuccess { story ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todayStory = story,
                            isContentLoaded = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun observeUserStats() {
        viewModelScope.launch {
            getUserStatsUseCase()
                .collect { stats ->
                    _uiState.update { it.copy(userStats = stats) }
                }
        }
    }

    fun retry() {
        loadInitialContent()
    }
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/screens/today/TodayScreen.kt`

```kotlin
package presentation.screens.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.LoadingState
import presentation.components.StreakWidget
import presentation.components.TodayStoryCard

@Composable
fun TodayScreen(
    viewModel: TodayViewModel = koinViewModel(),
    onStoryClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Story") }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingState(modifier = Modifier.fillMaxSize())
            }
            
            uiState.error != null -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = viewModel::retry,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
            
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Streak Widget
                    StreakWidget(
                        currentStreak = uiState.userStats.currentStreak,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Today's Story Card
                    uiState.todayStory?.let { story ->
                        TodayStoryCard(
                            story = story,
                            onClick = { onStoryClick(story.story.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
```

---

### Task 2.5: Library Screen (Days 16-17)

**File:** `composeApp/src/commonMain/kotlin/presentation/screens/library/LibraryViewModel.kt`

```kotlin
package presentation.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.Deity
import domain.model.Epic
import domain.model.StoryUiState
import domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LibraryUiState(
    val isLoading: Boolean = false,
    val stories: List<StoryUiState> = emptyList(),
    val filteredStories: List<StoryUiState> = emptyList(),
    val selectedDeity: Deity? = null,
    val selectedEpic: Epic? = null,
    val searchQuery: String = "",
    val error: String? = null
)

class LibraryViewModel(
    private val getAllStoriesUseCase: GetAllStoriesUseCase,
    private val filterByDeityUseCase: FilterStoriesByDeityUseCase,
    private val filterByEpicUseCase: FilterStoriesByEpicUseCase,
    private val searchStoriesUseCase: SearchStoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadStories()
    }

    private fun loadStories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getAllStoriesUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { stories ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            stories = stories,
                            filteredStories = stories
                        )
                    }
                }
        }
    }

    fun filterByDeity(deity: Deity?) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedDeity = deity, selectedEpic = null) }
            
            if (deity == null) {
                loadStories()
            } else {
                filterByDeityUseCase(deity)
                    .collect { stories ->
                        _uiState.update { it.copy(filteredStories = stories) }
                    }
            }
        }
    }

    fun filterByEpic(epic: Epic?) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedEpic = epic, selectedDeity = null) }
            
            if (epic == null) {
                loadStories()
            } else {
                filterByEpicUseCase(epic)
                    .collect { stories ->
                        _uiState.update { it.copy(filteredStories = stories) }
                    }
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchQuery = query,
                    selectedDeity = null,
                    selectedEpic = null
                )
            }
            
            if (query.isBlank()) {
                loadStories()
            } else {
                searchStoriesUseCase(query)
                    .collect { stories ->
                        _uiState.update { it.copy(filteredStories = stories) }
                    }
            }
        }
    }
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/screens/library/LibraryScreen.kt`

```kotlin
package presentation.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.Deity
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.FilterChips
import presentation.components.StoryCard

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onStoryClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            if (showSearch) {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::search,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            
            // Filter Chips
            FilterChips(
                selectedDeity = uiState.selectedDeity,
                onDeitySelected = viewModel::filterByDeity,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Story Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.filteredStories,
                    key = { it.story.id }
                ) { storyUiState ->
                    StoryCard(
                        story = storyUiState,
                        onClick = { onStoryClick(storyUiState.story.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search stories...") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        singleLine = true
    )
}
```

---

### Continue with remaining screens...

**Due to length constraints, I'll create separate files for:**
- Story Reader Screen
- Profile Screen  
- Onboarding Screen
- Reusable Components

Let me know if you want me to continue with the remaining tasks in Phase 2, or if you'd like me to create Phase 3 & 4 documents!

---

## ✅ Phase 2 Completion Checklist

- [ ] All repositories implemented
- [ ] All use cases created
- [ ] Navigation setup complete
- [ ] Today screen functional
- [ ] Library screen with filters
- [ ] Story reader screen
- [ ] Profile screen with stats
- [ ] Onboarding flow
- [ ] All reusable components
- [ ] Dummy data loads successfully
- [ ] All screens navigate correctly

**Time Estimate:** 4 weeks
**Next Phase:** Phase 3 - Data Integration & Features
