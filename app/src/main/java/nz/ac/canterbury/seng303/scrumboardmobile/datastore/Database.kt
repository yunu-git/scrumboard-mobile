package nz.ac.canterbury.seng303.scrumboardmobile.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import nz.ac.canterbury.seng303.scrumboardmobile.dao.StoryDao
import nz.ac.canterbury.seng303.scrumboardmobile.dao.TaskDao
import nz.ac.canterbury.seng303.scrumboardmobile.dao.UserDao
import nz.ac.canterbury.seng303.scrumboardmobile.dao.WorkLogDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import nz.ac.canterbury.seng303.scrumboardmobile.models.User
import nz.ac.canterbury.seng303.scrumboardmobile.models.WorkLog

@Database(entities = [Story::class, Task::class, WorkLog::class, User::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun taskDao(): TaskDao
    abstract fun workLogDao(): WorkLogDao
    abstract fun userDao(): UserDao

}