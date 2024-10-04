package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.StoryDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks

class StoryViewModel (private val storyDao: StoryDao): ViewModel() {
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
        storyDao.getAllStories().catch { Log.e("STORY_VIEW_MODEL", it.toString()) }
            .collect { _stories.emit(it) }
    }

    fun createStory(title: String,
                    description: String,
                    timeCreated: Long) = viewModelScope.launch {
        val story = Story(
            title = title,
            description = description,
            timeCreated = timeCreated
        )
        try {
            val storyId = storyDao.insertStory(story)
            Log.d("STORY_VIEW_MODEL", "Story has been created with id: $storyId")
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

}

