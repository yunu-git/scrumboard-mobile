package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithWorkLogs (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val workLogs: List<WorkLog>
){
}