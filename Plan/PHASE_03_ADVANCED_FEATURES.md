# Phase 3: Advanced Features & Polish (Weeks 7-8)

## 🎯 Goal
Implement audio playback, streak system, notifications, share functionality, and polish the UI.

## ✅ Deliverables
- [ ] Text-to-Speech audio playback
- [ ] Streak tracking with milestones
- [ ] Daily notifications
- [ ] Share functionality
- [ ] Preferences management
- [ ] All animations & polish

---

## 📋 Tasks Breakdown

### Task 3.1: Preferences Manager (Day 19)

**Goal:** Create DataStore-based preferences for app settings.

**File:** `shared/src/commonMain/kotlin/data/local/preferences/PreferencesManager.kt`

```kotlin
package data.local.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun isOnboardingCompleted(): Flow<Boolean>
    
    suspend fun setNotificationsEnabled(enabled: Boolean)
    fun isNotificationsEnabled(): Flow<Boolean>
    
    suspend fun setNotificationTime(hour: Int, minute: Int)
    fun getNotificationTime(): Flow<Pair<Int, Int>>
    
    suspend fun setContentLoaded(loaded: Boolean)
    fun isContentLoaded(): Flow<Boolean>
    
    suspend fun setDarkModeEnabled(enabled: Boolean)
    fun isDarkModeEnabled(): Flow<Boolean>
}
```

**File:** `shared/src/androidMain/kotlin/data/local/preferences/PreferencesManagerImpl.android.kt`

```kotlin
package data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mythly_preferences")

class PreferencesManagerImpl(
    private val context: Context
) : PreferencesManager {

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
        val NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
        val CONTENT_LOADED = booleanPreferencesKey("content_loaded")
        val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = completed }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    }

    override fun isNotificationsEnabled(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.NOTIFICATIONS_ENABLED] ?: true }

    override suspend fun setNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[Keys.NOTIFICATION_HOUR] = hour
            it[Keys.NOTIFICATION_MINUTE] = minute
        }
    }

    override fun getNotificationTime(): Flow<Pair<Int, Int>> =
        context.dataStore.data.map {
            Pair(
                it[Keys.NOTIFICATION_HOUR] ?: 7,
                it[Keys.NOTIFICATION_MINUTE] ?: 0
            )
        }

    override suspend fun setContentLoaded(loaded: Boolean) {
        context.dataStore.edit { it[Keys.CONTENT_LOADED] = loaded }
    }

    override fun isContentLoaded(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.CONTENT_LOADED] ?: false }

    override suspend fun setDarkModeEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_MODE_ENABLED] = enabled }
    }

    override fun isDarkModeEnabled(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.DARK_MODE_ENABLED] ?: false }
}
```

**Update DI:**

```kotlin
// In androidMain/di/PlatformModule.android.kt
actual val platformModule = module {
    // ... existing code ...
    
    single<PreferencesManager> { 
        PreferencesManagerImpl(androidContext()) 
    }
}
```

---

### Task 3.2: Audio Playback (TTS) (Days 20-21)

**Goal:** Implement text-to-speech audio for story narration.

**File:** `shared/src/commonMain/kotlin/util/TTSManager.kt`

```kotlin
package util

interface TTSManager {
    fun initialize(onReady: () -> Unit)
    fun speak(text: String)
    fun pause()
    fun resume()
    fun stop()
    fun isPlaying(): Boolean
    fun shutdown()
}
```

**File:** `shared/src/androidMain/kotlin/util/TTSManagerImpl.android.kt`

```kotlin
package util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class TTSManagerImpl(
    private val context: Context
) : TTSManager {
    
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isCurrentlyPlaying = false

    override fun initialize(onReady: () -> Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(0.9f) // Slightly slower for better comprehension
                isInitialized = true
                
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        isCurrentlyPlaying = true
                    }

                    override fun onDone(utteranceId: String?) {
                        isCurrentlyPlaying = false
                    }

                    override fun onError(utteranceId: String?) {
                        isCurrentlyPlaying = false
                    }
                })
                
                onReady()
            }
        }
    }

    override fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "story_utterance")
        }
    }

    override fun pause() {
        if (isInitialized) {
            tts?.stop()
            isCurrentlyPlaying = false
        }
    }

    override fun resume() {
        // TTS doesn't support true resume, would need to re-speak from position
    }

    override fun stop() {
        pause()
    }

    override fun isPlaying(): Boolean = isCurrentlyPlaying

    override fun shutdown() {
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/components/AudioPlayer.kt`

```kotlin
package presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlayer(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause Button
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(32.dp)
                )
            }
            
            // Stop Button
            IconButton(onClick = onStop) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop"
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Audio indicator
            if (isPlaying) {
                Text(
                    text = "Playing...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
```

**Update StoryReaderViewModel to include audio:**

```kotlin
// Add to StoryReaderViewModel
class StoryReaderViewModel(
    // ... existing params ...
    private val ttsManager: TTSManager
) : ViewModel() {
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    init {
        ttsManager.initialize {
            // TTS ready
        }
    }
    
    fun playAudio() {
        _uiState.value.story?.let { story ->
            ttsManager.speak(story.story.content)
            _isPlaying.value = true
        }
    }
    
    fun pauseAudio() {
        ttsManager.pause()
        _isPlaying.value = false
    }
    
    fun stopAudio() {
        ttsManager.stop()
        _isPlaying.value = false
    }
    
    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
```

---

### Task 3.3: Streak System (Day 22)

**Goal:** Implement visual streak tracking with milestones.

**File:** `composeApp/src/commonMain/kotlin/presentation/components/StreakWidget.kt`

```kotlin
package presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import presentation.theme.StreakFire

@Composable
fun StreakWidget(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fire Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(StreakFire.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = StreakFire,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                // Streak Count
                AnimatedContent(
                    targetState = currentStreak,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith
                                slideOutVertically { -it } + fadeOut()
                    }
                ) { streak ->
                    Text(
                        text = "$streak Day Streak",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                // Encouragement
                Text(
                    text = getEncouragementText(currentStreak),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun getEncouragementText(streak: Int): String = when {
    streak == 0 -> "Start your journey today!"
    streak < 7 -> "Keep it going! 🌟"
    streak < 30 -> "You're on fire! 🔥"
    streak < 100 -> "Amazing dedication! 💪"
    else -> "Legendary streak! 🏆"
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/components/StreakCalendar.kt`

```kotlin
package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import presentation.theme.Success

@Composable
fun StreakCalendar(
    readDays: Set<Int>, // Day of month (1-31)
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(31) { dayIndex ->
            val day = dayIndex + 1
            val isRead = readDays.contains(day)
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isRead) Success.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isRead) Success else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
```

---

### Task 3.4: Notifications (Day 23)

**Goal:** Implement daily story notifications.

**File:** `shared/src/androidMain/kotlin/util/NotificationManager.android.kt`

```kotlin
package util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class MythlyNotificationManager(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "daily_story_channel"
        private const val NOTIFICATION_ID = 1001
        private const val WORK_NAME = "daily_story_notification"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Story",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily mythology story notifications"
            }
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun scheduleDailyNotification(hour: Int, minute: Int) {
        val currentTime = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
        }
        
        if (calendar.timeInMillis <= currentTime) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        
        val delay = calendar.timeInMillis - currentTime
        
        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    fun cancelDailyNotification() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
    
    fun showNotification(title: String, message: String, storyId: String) {
        // Create deep link intent
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = android.net.Uri.parse("mythly://story/$storyId")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}

class DailyNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        // Get today's story title
        // For MVP, use a generic message
        val title = "🪔 Today's Story Awaits"
        val message = "Discover ancient wisdom in just 5 minutes"
        val storyId = "today" // Will be replaced with actual story ID
        
        MythlyNotificationManager(applicationContext).showNotification(title, message, storyId)
        
        return Result.success()
    }
}
```

---

### Task 3.5: Share Functionality (Day 24)

**Goal:** Implement story sharing to social media.

**File:** `shared/src/commonMain/kotlin/util/ShareManager.kt`

```kotlin
package util

interface ShareManager {
    fun shareStory(title: String, excerpt: String, deepLink: String)
}
```

**File:** `shared/src/androidMain/kotlin/util/ShareManagerImpl.android.kt`

```kotlin
package util

import android.content.Context
import android.content.Intent

class ShareManagerImpl(private val context: Context) : ShareManager {
    
    override fun shareStory(title: String, excerpt: String, deepLink: String) {
        val shareText = buildString {
            appendLine("📖 $title")
            appendLine()
            appendLine(excerpt.take(200) + "...")
            appendLine()
            appendLine("Read full story on Mythly app")
            appendLine(deepLink)
        }
        
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        
        val chooser = Intent.createChooser(intent, "Share story via")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooser)
    }
}
```

**Add to DI:**

```kotlin
// In androidMain/di/PlatformModule.android.kt
single<ShareManager> { ShareManagerImpl(androidContext()) }
single<TTSManager> { TTSManagerImpl(androidContext()) }
single { MythlyNotificationManager(androidContext()) }
```

---

### Task 3.6: UI Polish & Animations (Days 25-26)

**Goal:** Add smooth animations and polish UI.

**File:** `composeApp/src/commonMain/kotlin/presentation/components/LoadingState.kt`

```kotlin
package presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(alpha)
        )
    }
}
```

**File:** `composeApp/src/commonMain/kotlin/presentation/components/Animations.kt`

```kotlin
package presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> AnimatedListItem(
    item: T,
    content: @Composable (T) -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        content(item)
    }
}

fun Modifier.shakeAnimation(enabled: Boolean): Modifier {
    return this.then(
        if (enabled) {
            // Add shake animation logic
            this
        } else {
            this
        }
    )
}
```

---

## ✅ Phase 3 Completion Checklist

- [ ] Preferences manager implemented
- [ ] TTS audio playback working
- [ ] Streak widget displays correctly
- [ ] Streak calendar functional
- [ ] Daily notifications scheduled
- [ ] Share functionality works
- [ ] All animations smooth
- [ ] UI polished
- [ ] No performance issues
- [ ] App feels premium

**Time Estimate:** 2 weeks
**Next Phase:** Phase 4 - Testing & Launch Prep
