package nz.ac.canterbury.seng303.scrumboardmobile.models

import androidx.room.Embedded
import androidx.room.Relation

data class StoryWithTasks(
    @Embedded val story: Story,
    @Relation(
        parentColumn = "storyId",
        entityColumn = "storyId"
    )
    val tasks: List<Task>
) {

}