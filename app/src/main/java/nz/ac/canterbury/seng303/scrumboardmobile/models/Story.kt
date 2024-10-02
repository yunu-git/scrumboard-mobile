package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Story (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "Title") val title: String
)