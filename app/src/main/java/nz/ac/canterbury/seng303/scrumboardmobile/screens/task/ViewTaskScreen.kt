package nz.ac.canterbury.seng303.scrumboardmobile.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ViewTaskScreen(
    navController: NavController,
    taskViewModel: TaskViewModel,
    storyId: String,
    taskId: String
) {
    taskViewModel.getTaskWithWorkLogs(
        storyId = storyId.toIntOrNull(),
        taskId = taskId.toIntOrNull()
    )
    val selectedTaskState by taskViewModel.selectedTaskWithWorkLogs.collectAsState(null)
    val taskWithWorkLogs: TaskWithWorkLogs? = selectedTaskState

    if (taskWithWorkLogs != null) {
        Scaffold(
            floatingActionButton = {
                ExtendedCreateWorkLogFab(
                    navController = navController,
                    storyId = storyId,
                    taskId = taskId
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                //TODO add your task logic and components here...


                ///////////////////////// this is temporaty stuff to check worklogs /////////////////////////////
                Text("Title: ${taskWithWorkLogs.task.title}")
                Text("Description: ${taskWithWorkLogs.task.description}")
                Text("Status: ${taskWithWorkLogs.task.status}")

                Spacer(modifier = Modifier.height(16.dp))

                // Display work logs
                Text("Work Logs:", style = MaterialTheme.typography.titleLarge)
                taskWithWorkLogs.workLogs.forEach { workLog ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(workLog.time)
                            )}")
                            Text("Hours: ${workLog.workingHours}")
                            Text("Description: ${workLog.description}")
                        }
                    }
                }


///////////////////////// up to here /////////////////////////////

            }
        }
    }
}

@Composable
fun ExtendedCreateWorkLogFab(
    navController: NavController,
    storyId: String,
    taskId: String
) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate("Story/$storyId/Task/$taskId/CreateWorkLog") },
        text = { Text(text = "Add Work Log") },
        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add WorkLog") }
    )
}