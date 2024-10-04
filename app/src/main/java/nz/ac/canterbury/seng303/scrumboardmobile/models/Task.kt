package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val storyId: Int,
    val title: String,
    val description: String,
    val assignedTo: Int?,
    val complexity: ScrumboardConstants.Complexity,
    val status: ScrumboardConstants.Status,
    val estimate: Int,
    val priority: ScrumboardConstants.Priority,
    val reviewerId: Int?
) {
}