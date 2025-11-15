# Mythly KMP - Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Prerequisites
```bash
# Required
- Android Studio Ladybug | 2024.2.1+ (with KMP support)
- JDK 17+
- Android SDK 34
- Kotlin 1.9.22+

# Recommended
- 16GB RAM
- SSD storage
- Android device/emulator for testing
```

### Initial Setup

1. **Clone & Open Project**
```bash
git clone <your-repo>
cd mythly-kmp
open with Android Studio
```

2. **Sync Dependencies**
```bash
./gradlew clean build
# Wait for Gradle sync to complete
```

3. **Run the App**
```bash
# From Android Studio: Run > Run 'androidApp'
# Or from terminal:
./gradlew :androidApp:installDebug
```

---

## 📁 Project Navigation

### Key Directories

```
mythly-kmp/
├── 📱 androidApp/          # Android app entry point
├── 🎨 composeApp/          # Shared Compose UI
│   └── presentation/       # All UI screens & components
├── 🧩 shared/              # Shared business logic
│   ├── domain/            # Models, UseCases, Interfaces
│   ├── data/              # Repositories, DAOs, Database
│   └── di/                # Dependency Injection
└── 📚 docs/               # Documentation
    ├── phases/            # Phase-wise execution plans
    └── ARCHITECTURE.md    # Architecture guide
```

---

## 🎯 Development Workflow

### Daily Development Cycle

1. **Pick a task** from current phase document
2. **Create feature branch**
   ```bash
   git checkout -b feature/task-name
   ```
3. **Implement** following the phase guide
4. **Test locally**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```
5. **Commit & push**
   ```bash
   git add .
   git commit -m "feat: implement task XYZ"
   git push
   ```

### Phase Progression

```
Week 1-2:  Phase 1 (Foundation)
Week 3-6:  Phase 2 (Core Features)
Week 7-8:  Phase 3 (Advanced Features)
Week 9-10: Phase 4 (Testing & Launch)
```

**Current Focus:** See `docs/phases/PHASE_01_FOUNDATION.md`

---

## 🔧 Common Commands

### Build Commands
```bash
# Clean build
./gradlew clean build

# Debug APK
./gradlew :androidApp:assembleDebug

# Release AAB
./gradlew :androidApp:bundleRelease

# Run tests
./gradlew test
./gradlew :shared:testDebugUnitTest
```

### Code Quality
```bash
# Lint check
./gradlew lint

# Format code
./gradlew ktlintFormat

# Dependency updates
./gradlew dependencyUpdates
```

### Database
```bash
# Export Room schema
./gradlew :shared:kspDebugKotlinAndroid

# Check schema location
ls shared/schemas/
```

---

## 🐛 Troubleshooting

### Gradle Sync Issues
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/
./gradlew clean
# Restart Android Studio
```

### Room Compilation Errors
```bash
# Regenerate Room code
./gradlew :shared:kspDebugKotlinAndroid --rerun-tasks
```

### KMP Issues
```bash
# Clear KMP cache
./gradlew cleanKotlinMultiplatform
```

### Dependency Conflicts
```bash
# Check dependency tree
./gradlew :androidApp:dependencies
```

---

## 📝 Code Conventions

### Naming
```kotlin
// Files: PascalCase
StoryRepository.kt
TodayViewModel.kt

// Classes: PascalCase
class TodayViewModel

// Functions: camelCase
fun loadTodayStory()

// Variables: camelCase
val todayStory = ...

// Constants: SCREAMING_SNAKE_CASE
const val MAX_STORIES = 100
```

### Package Structure
```kotlin
package domain.model        // Domain models
package domain.usecase      // Use cases
package domain.repository   // Repository interfaces

package data.repository     // Repository implementations
package data.local.dao      // Room DAOs
package data.local.entity   // Room entities

package presentation.screens.today    // Today screen
package presentation.components       // Reusable components
package presentation.theme           // Theme & styling
```

### Comments
```kotlin
// Use KDoc for public APIs
/**
 * Loads today's story based on current date.
 *
 * @return Story for the current day or null if unavailable
 */
suspend fun getTodayStory(): Story?

// Use inline comments sparingly
// TODO: Add caching logic
// FIXME: Handle edge case
```

---

## 🎨 Design System Quick Reference

### Colors
```kotlin
import presentation.theme.*

// Primary
SaffronPrimary      // #FF9933
SkyBlue            // #64B5F6
GoldenYellow       // #FFC107

// Semantic
StreakFire         // #FF5722 (for streak widget)
Success            // #4CAF50
Error              // #F44336
```

### Typography
```kotlin
import androidx.compose.material3.MaterialTheme

// In Composables
Text(
    text = "Story Title",
    style = MaterialTheme.typography.headlineMedium
)

// Available styles:
displayLarge/Medium/Small
headlineLarge/Medium/Small
titleLarge/Medium/Small
bodyLarge/Medium/Small
labelLarge/Medium/Small
```

### Spacing
```kotlin
import androidx.compose.ui.unit.dp

Spacer(modifier = Modifier.height(8.dp))   // Tight
Spacer(modifier = Modifier.height(16.dp))  // Normal
Spacer(modifier = Modifier.height(24.dp))  // Comfortable
Spacer(modifier = Modifier.height(32.dp))  // Spacious
```

---

## 🔌 Dependency Injection

### Using Koin

```kotlin
// In ViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodayScreen(
    viewModel: TodayViewModel = koinViewModel()
) {
    // Use viewModel
}

// In regular class
import org.koin.core.component.inject

class MyClass : KoinComponent {
    private val repository: StoryRepository by inject()
}

// In plain function
import org.koin.mp.KoinPlatform.getKoin

fun someFunction() {
    val repository = getKoin().get<StoryRepository>()
}
```

### Adding New Dependencies

```kotlin
// 1. Add to module (e.g., di/DomainModule.kt)
val domainModule = module {
    factory { MyNewUseCase(get()) }
}

// 2. Inject where needed
class MyViewModel(
    private val myNewUseCase: MyNewUseCase  // Auto-injected by Koin
) : ViewModel()
```

---

## 📊 Database Access

### Using DAOs

```kotlin
// In Repository
class StoryRepositoryImpl(
    private val storyDao: StoryDao
) {
    // Flow (reactive)
    fun getAllStories(): Flow<List<Story>> =
        storyDao.getAllStories()
    
    // Suspend (one-time)
    suspend fun getStory(id: String): Story? =
        storyDao.getStoryById(id)
}

// In ViewModel
viewModelScope.launch {
    repository.getAllStories()
        .collect { stories ->
            // Update UI state
        }
}
```

### Adding New Queries

```kotlin
// 1. Add to DAO
@Dao
interface StoryDao {
    @Query("SELECT * FROM stories WHERE title LIKE :query")
    fun searchByTitle(query: String): Flow<List<StoryEntity>>
}

// 2. Use in Repository
override fun searchByTitle(query: String): Flow<List<Story>> =
    storyDao.searchByTitle("%$query%")
        .map { entities -> entities.map { it.toDomain() } }
```

---

## 🧪 Testing Quick Guide

### Unit Test Template
```kotlin
import kotlin.test.*
import kotlinx.coroutines.test.runTest
import io.mockk.*

class MyClassTest {
    private lateinit var subject: MyClass
    
    @BeforeTest
    fun setup() {
        subject = MyClass()
    }
    
    @Test
    fun `test name in backticks`() = runTest {
        // Given
        val input = "test"
        
        // When
        val result = subject.doSomething(input)
        
        // Then
        assertEquals("expected", result)
    }
}
```

### Running Tests
```bash
# All tests
./gradlew test

# Specific module
./gradlew :shared:testDebugUnitTest

# Specific test
./gradlew test --tests "MyClassTest"

# With coverage
./gradlew jacocoTestReport
```

---

## 📱 Running on Device

### Debug Build
```bash
# Install
./gradlew :androidApp:installDebug

# Install and run
./gradlew :androidApp:installDebug && adb shell am start -n com.mythly.app/.MainActivity

# View logs
adb logcat | grep "Mythly"
```

### Release Build
```bash
# Create signed AAB
./gradlew :androidApp:bundleRelease

# Output location:
# androidApp/build/outputs/bundle/release/androidApp-release.aab
```

---

## 🆘 Getting Help

### Internal Resources
- 📖 [Architecture Guide](./ARCHITECTURE.md)
- 📋 [Phase Plans](./phases/)
- 📝 [README](../README.md)

### External Resources
- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Koin Documentation](https://insert-koin.io/)

### Common Questions

**Q: How do I add a new screen?**
1. Create screen file in `composeApp/src/commonMain/kotlin/presentation/screens/`
2. Create ViewModel
3. Add route to `NavGraph.kt`
4. Add to bottom nav if needed

**Q: How do I add a new story?**
1. Add to `dummy_stories.json`
2. Database will auto-populate on next launch

**Q: How do I change the app theme?**
- Modify `presentation/theme/Color.kt` and `Theme.kt`

**Q: How do I add a new dependency?**
1. Add to `gradle/libs.versions.toml`
2. Add to appropriate `build.gradle.kts`
3. Sync Gradle

---

## ⚡ Pro Tips

1. **Use Compose Previews** for rapid UI development
   ```kotlin
   @Preview
   @Composable
   fun PreviewStoryCard() {
       MythlyTheme {
           StoryCard(story = sampleStory)
       }
   }
   ```

2. **Hot Reload** - Make UI changes without rebuilding
   - Change composable code
   - Press Ctrl+S (or Cmd+S)
   - UI updates automatically

3. **Logcat Filtering** - Find your logs faster
   ```bash
   adb logcat -s "Mythly"
   ```

4. **Database Inspector** - View Room DB in Android Studio
   - View > Tool Windows > App Inspection > Database Inspector

5. **Profile Performance** - Use Android Profiler
   - View > Tool Windows > Profiler

---

## 🎯 Your First Contribution

### Getting Started (30 minutes)

1. **Read Phase 1** (`docs/phases/PHASE_01_FOUNDATION.md`)
2. **Start with Task 1.1** (Project Initialization)
3. **Follow the steps exactly**
4. **Test after each step**
5. **Commit your progress**

### Success Checklist
- [ ] Project compiles
- [ ] App launches on emulator
- [ ] Theme displays correctly
- [ ] Database creates successfully
- [ ] No errors in Logcat

---

**Happy Coding! 🚀**

For questions or issues, refer to phase documents or architecture guide.
