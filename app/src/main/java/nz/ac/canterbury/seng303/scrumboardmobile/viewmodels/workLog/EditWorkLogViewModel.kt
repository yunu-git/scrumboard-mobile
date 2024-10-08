package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.WorkLogDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.WorkLog

class EditWorkLogViewModel(private val workLogDao: WorkLogDao) : ViewModel() {

    fun updateWorkLog(
        workLog: WorkLog
    ) {
        viewModelScope.launch {
            workLogDao.updateWorkLog(workLog)
        }
    }

}