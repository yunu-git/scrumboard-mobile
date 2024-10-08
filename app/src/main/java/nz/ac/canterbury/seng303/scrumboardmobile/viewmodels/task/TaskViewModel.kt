package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.TaskDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs

class TaskViewModel (private val taskDao: TaskDao): ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks
    private val _selectedTaskWithWorkLogs = MutableStateFlow<TaskWithWorkLogs?>(null)
    val selectedTaskWithWorkLogs: StateFlow<TaskWithWorkLogs?> get() = _selectedTaskWithWorkLogs

    var taskTitle by mutableStateOf("")
    fun updateTaskTittle(newTitle: String) {
        taskTitle = newTitle
    }

    var taskDescription by mutableStateOf("")
    fun updateTaskDescription(newDescription: String) {
        taskDescription = newDescription
    }

    var status by mutableStateOf(ScrumboardConstants.Status.TO_DO)
    fun updateStatus(newStatus: ScrumboardConstants.Status) {
        status = newStatus
    }

    var estimate by mutableStateOf("")
    fun updateEstimate(newEstimate: String) {
        estimate = newEstimate
    }

    var priority by mutableStateOf(ScrumboardConstants.Priority.UNSET)
    fun updatePriority(newPriority: ScrumboardConstants.Priority) {
        priority = newPriority
    }

    var complexity by mutableStateOf(ScrumboardConstants.Complexity.UNSET)
    fun updateComplexity(newComplexity: ScrumboardConstants.Complexity) {
        complexity = newComplexity
    }


    fun getTaskWithWorkLogs(storyId: Int?, taskId: Int?) = viewModelScope.launch {
        if (storyId != null && taskId != null) {
            _selectedTaskWithWorkLogs.value = taskDao.getTasksWithWorkLogs(
                storyId = storyId,
                taskId = taskId
            ).first()
        } else {
            _selectedTaskWithWorkLogs.value = null
        }

    }

    // Updated function to update a task's status
    fun updateTask(task: Task) = viewModelScope.launch {
        try {
            taskDao.updateTask(task) // Call DAO to update the task
            Log.d("TASK_VIEW_MODEL", "Task status updated successfully")
        } catch (e: Exception) {
            Log.e("TASK_VIEW_MODEL", "Error updating task status", e)
        }
    }

    fun updateTaskFromState() = viewModelScope.launch {
        val taskWithWorkLogs = selectedTaskWithWorkLogs.value
        if (taskWithWorkLogs != null) {
            val updatedTask = taskWithWorkLogs.task.copy(
                title = taskTitle,
                description = taskDescription,
                status = status,
                estimate = estimate.toIntOrNull() ?: 0, // Ensure estimate is an integer
                priority = priority,
                complexity = complexity
            )
            try {
                taskDao.updateTask(updatedTask)
                Log.d("TASK_VIEW_MODEL", "Task updated successfully")
                _selectedTaskWithWorkLogs.value = taskWithWorkLogs.copy(task = updatedTask)
            } catch (e: Exception) {
                Log.e("TASK_VIEW_MODEL", "Error updating task", e)
            }
        } else {
            Log.w("TASK_VIEW_MODEL", "No task selected for updating")
        }
    }

    fun createTask(title: String,
                    description: String,
                    complexity: ScrumboardConstants.Complexity,
                   priority: ScrumboardConstants.Priority,
                   estimate: Int,
                   storyId: Int
                   ) = viewModelScope.launch {
        val task = Task(
            title = title,
            description = description,
            complexity = complexity,
            priority = priority,
            estimate = estimate,
            status = ScrumboardConstants.Status.TO_DO,
            assignedTo = null,
            storyId = storyId,
            reviewerId = null
        )
        try {
            val taskId = taskDao.insertTask(task)
            Log.d("STORY_VIEW_MODEL", "Story has been created with id: $taskId")
        } catch (e: Exception) {
            Log.e("STORY_VIEW_MODEL", "Could not insert Story", e)
        }
    }

        fun setTaskProperties(taskWithWorkLogs: TaskWithWorkLogs) {
            val task = taskWithWorkLogs.task
            taskTitle = task.title
            taskDescription = task.description
            status = task.status
            estimate = task.estimate.toString()
            priority = task.priority
            complexity = task.complexity
    }
}