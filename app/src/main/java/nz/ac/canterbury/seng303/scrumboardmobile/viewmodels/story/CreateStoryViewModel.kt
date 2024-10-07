package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

class CreateStoryViewModel: ViewModel() {

    var title by mutableStateOf("")
    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    var description by mutableStateOf("")
    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    var dueAt by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    fun updateDueAt(newDueAt: LocalDateTime) {
        dueAt = newDueAt
    }

    fun clearFields() {
        title = ""
        description = ""
        dueAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

}