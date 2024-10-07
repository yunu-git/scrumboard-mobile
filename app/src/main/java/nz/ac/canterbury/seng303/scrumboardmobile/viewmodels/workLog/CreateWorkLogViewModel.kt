package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

class CreateWorkLogViewModel : ViewModel() {
    var description by mutableStateOf("")
        private set

    var time by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        private set

    var workingHours by mutableStateOf("")
        private set

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateTime(newTime: LocalDate) {
        time = newTime
    }

    fun updateWorkingHours(newWorkingHours: String) {
        workingHours = newWorkingHours
    }

    fun clearInputs() {
        description = ""
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        workingHours = ""
    }
}