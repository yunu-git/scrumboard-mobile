package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants

class CreateTaskViewModel: ViewModel() {

    var title by mutableStateOf("")
    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    var description by mutableStateOf("")
    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    var priority by mutableStateOf(ScrumboardConstants.Priority.UNSET)
    fun updatePriority(newPriority: ScrumboardConstants.Priority) {
        priority = newPriority
    }

    var complexity by mutableStateOf(ScrumboardConstants.Complexity.UNSET)
    fun updateComplexity(newComplexity: ScrumboardConstants.Complexity) {
        complexity = newComplexity
    }

    var estimate by mutableIntStateOf(0)
    fun updateEstimate(newEstimate: Int) {
        estimate = newEstimate
    }
}