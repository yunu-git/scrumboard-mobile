package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import nz.ac.canterbury.seng303.scrumboardmobile.MainApplication
import nz.ac.canterbury.seng303.scrumboardmobile.R
import nz.ac.canterbury.seng303.scrumboardmobile.dao.StoryDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.notification.NotificationScheduler
import nz.ac.canterbury.seng303.scrumboardmobile.notification.NotificationWorker
import java.util.Date
import java.util.concurrent.TimeUnit

class StoryViewModel (private val storyDao: StoryDao, private val context: Context): ViewModel() {

    private val notificationScheduler = NotificationScheduler(context)

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> get() = _stories

    private val _selectedStoryWithTasks = MutableStateFlow<StoryWithTasks?>(null)
    val selectedStoryWithTasks: StateFlow<StoryWithTasks?> = _selectedStoryWithTasks

    private val _storiesWithTasks = MutableStateFlow<List<StoryWithTasks>>(emptyList())
    val storiesWithTasks: StateFlow<List<StoryWithTasks>> get() = _storiesWithTasks

    fun getStoriesWithTasks() {
        viewModelScope.launch {
            storyDao.getAllStories().collect { stories ->
                val tasksFlows = stories.map { story ->
                    storyDao.getStoryWithTasks(story.storyId)
                }
                combine(tasksFlows) { storyWithTasksList ->
                    storyWithTasksList.toList()
                }.collect { combinedList ->
                    _storiesWithTasks.value = combinedList
                }
            }
        }
    }

    fun getStories() = viewModelScope.launch {
        storyDao.getAllStoriesWithTasks().catch { Log.e("STORY_VIEW_MODEL", it.toString()) }
            .collect {_storiesWithTasks.emit(it) }
    }

    fun createStory(title: String,
                    description: String,
                    timeCreated: Long,
                    dueAt: LocalDateTime
    ) = viewModelScope.launch {
        val story = Story(
            title = title,
            description = description,
            timeCreated = timeCreated,
            dueAt = dueAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
        try {
            val storyId = storyDao.insertStory(story)
            Log.d("STORY_VIEW_MODEL", "Story has been created with id: $storyId")
            scheduleNotifications("$title is due now.", dueAt)
        } catch (e: Exception) {
            Log.e("STORY_VIEW_MODEL", "Could not insert Story", e)
        }
    }

    fun getStoryWithTasks(storyId: Int?) = viewModelScope.launch {
        if (storyId != null) {
            _selectedStoryWithTasks.value = storyDao.getStoryWithTasks(storyId).first()
        } else {
            _selectedStoryWithTasks.value = null
        }
    }

    private fun scheduleNotifications(message: String, scheduleDate: LocalDateTime) {
        Log.d("STORY_VIEW_MODEL", "Scheduling notification for $scheduleDate")


        val currentTime = System.currentTimeMillis()
        val scheduledTimeMillis = scheduleDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val initialDelay = scheduledTimeMillis - currentTime

        val inputData = workDataOf(
            "title" to context.getString(R.string.notification_title),
            "message" to message
        )

        notificationScheduler.scheduleNotification(
            context.getString(R.string.notification_title),
            message,
            initialDelay
        )
    }

}

