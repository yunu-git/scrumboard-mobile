package nz.ac.canterbury.seng303.scrumboardmobile.screens.workLog

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.datetime.Instant
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog.CreateWorkLogViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.workLog.WorkLogViewModel
import kotlinx.datetime.*

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
    val context = LocalContext.current

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
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = createWorkLogViewModel.time.toString(),
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = createWorkLogViewModel.workingHours,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        createWorkLogViewModel.updateWorkingHours(newValue)
                    }
                },
                label = { Text("Hours") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val timeMillis = createWorkLogViewModel.time.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

                when {
                    createWorkLogViewModel.description.trim().isEmpty() -> {
                        Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    createWorkLogViewModel.workingHours.toIntOrNull()?.let { it <= 0 } ?: true -> {
                        Toast.makeText(context, "Enter valid working hour(s)", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        workLogViewModel.createWorkLog(
                            taskId = taskId,
                            description = createWorkLogViewModel.description,
                            time = timeMillis,
                            workingHours = createWorkLogViewModel.workingHours.toIntOrNull() ?: 0,
                            createdById = createdById
                        )
                        createWorkLogViewModel.clearInputs()
                        navController.popBackStack()
                    }
                }
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
                            val localDate = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                            createWorkLogViewModel.updateTime(localDate)
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

    DisposableEffect(Unit) {
        onDispose {
            createWorkLogViewModel.clearInputs()
        }
    }
}
