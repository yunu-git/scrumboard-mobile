package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkLog(
    @PrimaryKey(autoGenerate = true) val workLogId: Int,
    val taskId: Int,
    val description: String,
    val time: Long,
    val workingHours: Int,
    val createdById: Int
) {

}