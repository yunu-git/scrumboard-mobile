package nz.ac.canterbury.seng303.scrumboardmobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import nz.ac.canterbury.seng303.scrumboardmobile.models.WorkLog

@Dao
interface WorkLogDao {
    @Insert
    suspend fun insertWorkLog(vararg workLog: WorkLog)
    @Update
    suspend fun updateWorkLog(vararg workLog: WorkLog)
    @Delete
    suspend fun deleteWorkLog(vararg workLog: WorkLog)
}