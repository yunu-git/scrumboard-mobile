package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.WorkLogDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.WorkLog

class WorkLogViewModel(private val workLogDao: WorkLogDao) : ViewModel() {

    fun createWorkLog(
                taskId: Int,
                description: String,
                time: Long,
                workingHours: Int,
                createdById: Int
    ) {
        viewModelScope.launch {
            val workLog = WorkLog(0, taskId, description, time, workingHours, createdById)
            workLogDao.insertWorkLog(workLog)
        }
    }
}