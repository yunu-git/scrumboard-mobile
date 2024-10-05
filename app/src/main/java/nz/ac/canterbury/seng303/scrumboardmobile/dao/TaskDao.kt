package nz.ac.canterbury.seng303.scrumboardmobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(vararg task: Task)
    @Update
    suspend fun updateTask(vararg task: Task)
    @Delete
    suspend fun deleteTask(vararg task: Task)
    @Transaction
    @Query("SELECT * FROM Task WHERE storyId = :storyId AND taskId = :taskId")
    fun getTasksWithWorkLogs(storyId: Int, taskId: Int): Flow<TaskWithWorkLogs>
    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>
}