package nz.ac.canterbury.seng303.scrumboardmobile.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants

@Composable
fun CreateTaskScreen (
    navController: NavController,

    title: String,
    onTitleChange: (String) -> Unit,

    description: String,
    onDescriptionChange: (String) -> Unit,

    estimate: Int,
    onEstimateChange: (Int) -> Unit,

    selectedPriority: ScrumboardConstants.Priority,
    onPriorityChange: (ScrumboardConstants.Priority) -> Unit,

    selectedComplexity: ScrumboardConstants.Complexity,
    onComplexityChange: (ScrumboardConstants.Complexity) -> Unit,

    createTaskFn: (String, String, Int, ScrumboardConstants.Priority, ScrumboardConstants.Complexity) -> Unit
) {
    var expandedPriority by remember { mutableStateOf(false) }
    var expandedComplexity by remember { mutableStateOf(false) }
    var integerValue by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a Task for your story")
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text("Task Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Priority selection dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedPriority.priority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Task Priority") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedPriority = true },
                    trailingIcon = {
                        IconButton(onClick = { expandedPriority = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Task Priority")
                        }
                    }
                )

                DropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Iterate through the enum values to create dropdown items
                    ScrumboardConstants.Priority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = {
                                Text(text = priority.priority)
                            },
                            onClick = {
                                onPriorityChange(priority)
                                expandedPriority = false
                            }
                        )
                    }
                }
            }

            // Complexity selection dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedComplexity.complexity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Task Complexity") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedComplexity = true },
                    trailingIcon = {
                        IconButton(onClick = { expandedComplexity = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Task Complexity")
                        }
                    }
                )

                DropdownMenu(
                    expanded = expandedComplexity,
                    onDismissRequest = { expandedComplexity = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Iterate through the enum values to create dropdown items
                    ScrumboardConstants.Complexity.entries.forEach { complexity ->
                        DropdownMenuItem(
                            text = {
                                Text(text = complexity.complexity)
                            },
                            onClick = {
                                onComplexityChange(complexity)
                                expandedComplexity = false
                            }
                        )
                    }
                }
            }

            // Estimate
            OutlinedTextField(
                value = integerValue,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                        integerValue = it
                    }

                    if (it.all { char -> char.isDigit()} && it != "") {
                        onEstimateChange(it.toInt())
                    }
                },
                label = { Text("Task Estimate") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    when {
                        title.trim().isEmpty() -> {
                            Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        description.trim().isEmpty() -> {
                            Toast.makeText(context, "Description cannot be unset", Toast.LENGTH_SHORT).show()
                        }
                        selectedPriority == ScrumboardConstants.Priority.UNSET -> {
                            Toast.makeText(context, "Priority cannot be unset", Toast.LENGTH_SHORT).show()
                        }
                        selectedComplexity == ScrumboardConstants.Complexity.UNSET -> {
                            Toast.makeText(context, "Complexity cannot be unset", Toast.LENGTH_SHORT).show()
                        }
                        integerValue == "" -> {
                            Toast.makeText(context, "Estimate cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        estimate <= 0 -> {
                            Toast.makeText(context, "Estimate must be a positive integer", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            createTaskFn(title, description, estimate, selectedPriority, selectedComplexity)
                            onTitleChange("")
                            onDescriptionChange("")
                            onPriorityChange(ScrumboardConstants.Priority.UNSET)
                            onComplexityChange(ScrumboardConstants.Complexity.UNSET)
                            navController.navigate("AllStories")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Create Task")
            }
        }
    }
}