package nz.ac.canterbury.seng303.scrumboardmobile.screens.workLog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog.CreateWorkLogViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog.WorkLogViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkLogScreen(
    navController: NavController,
    createWorkLogViewModel: CreateWorkLogViewModel,
    workLogViewModel: WorkLogViewModel,
    taskId: Int,
    createdById: Int
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = createWorkLogViewModel.description,
            onValueChange = { createWorkLogViewModel.updateDescription(it) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = createWorkLogViewModel.time,
                onValueChange = { },
                label = { Text("Date") },
                modifier = Modifier.weight(1f),
                readOnly = true
            )
            TextButton(onClick = { showDatePicker = true }) {
                Text("Select Date")
            }


            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = createWorkLogViewModel.workingHours,
                onValueChange = { createWorkLogViewModel.updateWorkingHours(it) },
                label = { Text("Hours") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val timeMillis = dateFormat.parse(createWorkLogViewModel.time)?.time ?: System.currentTimeMillis()

                workLogViewModel.createWorkLog(
                    taskId = taskId,
                    description = createWorkLogViewModel.description,
                    time = timeMillis,
                    workingHours = createWorkLogViewModel.workingHours.toIntOrNull() ?: 0,
                    createdById = createdById
                )
                createWorkLogViewModel.clearInputs()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Work Log")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val date = Date(it)
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            createWorkLogViewModel.updateTime(formatter.format(date))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}