package nz.ac.canterbury.seng303.scrumboardmobile.screens.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.TaskWithWorkLogs
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task.TaskViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ViewTaskScreen(
    navController: NavController,
    taskViewModel: TaskViewModel,
    userViewModel: UserViewModel,
    storyId: String,
    taskId: String
) {
    taskViewModel.getTaskWithWorkLogs(
        storyId = storyId.toIntOrNull(),
        taskId = taskId.toIntOrNull()
    )
    val selectedTaskState by taskViewModel.selectedTaskWithWorkLogs.collectAsState(null)
    val taskWithWorkLogs: TaskWithWorkLogs? = selectedTaskState
    val coroutineScope = rememberCoroutineScope()
    var expandedStatus by remember { mutableStateOf(false) }
    var assignedStatus by remember { mutableStateOf(ScrumboardConstants.Status.TO_DO) }
    val usernames = remember { mutableStateOf(mutableMapOf<Int, String>()) }

    LaunchedEffect(taskWithWorkLogs) {
        taskWithWorkLogs?.workLogs?.forEach { workLog ->
            coroutineScope.launch {
                val username = userViewModel.getUserById(workLog.userId)?.username ?: "Unknown User"
                usernames.value = usernames.value.toMutableMap().apply { put(workLog.userId, username) }
            }
        }
    }

    val scrollState = rememberScrollState()

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
                    .verticalScroll(scrollState)
            ) {

                    // Title
                Text(
                    text = taskWithWorkLogs.task.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Row to separate content into main details and sidebar
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Column for task title and description
                        Column(
                            modifier = Modifier.weight(3f)
                        ) {

                            // Description (up to 512 chars)
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = taskWithWorkLogs.task.description.take(512),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Sidebar for status, assignedTo, estimate, priority, complexity
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .wrapContentWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Status
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = assignedStatus.status,
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 12.sp),
                                    label = { Text("Task Priority") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp)
                                        .clickable { expandedStatus = true },
                                    trailingIcon = {
                                        IconButton(onClick = { expandedStatus = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Task Priority")
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = expandedStatus,
                                    onDismissRequest = { expandedStatus = false }
                                ) {

                                    // Iterate through the enum values to create dropdown items
                                    ScrumboardConstants.Status.entries.forEach { status ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = status.status)
                                            },
                                            onClick = {
                                                assignedStatus = status
                                                expandedStatus = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Estimate
                            Text(
                                text = "Estimate: ${taskWithWorkLogs.task.estimate}",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End
                            )

                            // Priority
                            Text(
                                text = "Priority: ${taskWithWorkLogs.task.priority.priority}",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End
                            )

                            // Complexity
                            Text(
                                text = "Complexity: ${taskWithWorkLogs.task.complexity.complexity}",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                // Display work logs
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Work Logs",
                    style = MaterialTheme.typography.headlineMedium
                )
                taskWithWorkLogs.workLogs.forEach { workLog ->
                    ElevatedCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Date in the top-left
                                Text(
                                    text =
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                            Date(workLog.time)
                                        ),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )

                                // Hours in a chip at the top-right
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text("${workLog.workingHours}h")
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.primary, // Use primary theme color
                                        labelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }


                            // Description
                            Text(workLog.description)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(text = "Added by ${usernames.value[workLog.userId] ?: "Loading..."}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(90.dp))
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