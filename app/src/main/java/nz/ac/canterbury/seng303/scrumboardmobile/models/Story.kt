package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Story (
    @PrimaryKey(autoGenerate = true) val storyId: Int,
    val title: String,
    val description: String,
    val timeCreated: Long,
)

