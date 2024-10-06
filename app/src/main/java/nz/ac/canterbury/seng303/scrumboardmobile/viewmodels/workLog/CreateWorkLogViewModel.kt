package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateWorkLogViewModel : ViewModel() {
    var description by mutableStateOf("")
        private set

    var time by mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        private set

    var workingHours by mutableStateOf("")
        private set

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateTime(newTime: String) {
        time = newTime
    }

    fun updateWorkingHours(newWorkingHours: String) {
        workingHours = newWorkingHours
    }

    fun clearInputs() {
        description = ""
        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        workingHours = ""
    }
}