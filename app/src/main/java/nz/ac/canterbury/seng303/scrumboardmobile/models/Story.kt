package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import java.util.Date

@Entity
data class Story(
    @PrimaryKey(autoGenerate = true) val storyId: Int = 0,
    val title: String,
    val description: String,
    val timeCreated: Long,
    val dueAt: Long
)

