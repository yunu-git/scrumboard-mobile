package nz.ac.canterbury.seng303.scrumboardmobile.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs
@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    fun getTaskWithWorkLogs(taskId: Int): Flow<TaskWithWorkLogs>
}