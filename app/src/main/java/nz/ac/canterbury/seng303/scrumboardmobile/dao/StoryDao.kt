package nz.ac.canterbury.seng303.scrumboardmobile.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs

@Dao
interface StoryDao {
    @Query("SELECT * FROM STORY")
    fun getAll(): List<Story>
    @Transaction
    @Query("SELECT * FROM Story WHERE storyId = :storyId")
    fun getStoryWithTasks(storyId: Int): Flow<StoryWithTasks>
}