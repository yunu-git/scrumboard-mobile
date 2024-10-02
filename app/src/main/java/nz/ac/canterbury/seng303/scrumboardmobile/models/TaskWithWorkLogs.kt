package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithWorkLogs(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val workLogs: List<WorkLog>
) {
}