package nz.ac.canterbury.seng303.scrumboardmobile.screens

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

    selectedPriority: ScrumboardConstants.Priority,
    onPriorityChange: (ScrumboardConstants.Priority) -> Unit,

    selectedComplexity: ScrumboardConstants.Complexity,
    onComplexityChange: (ScrumboardConstants.Complexity) -> Unit,

    createTaskFn: (String, String, ScrumboardConstants.Priority, ScrumboardConstants.Complexity) -> Unit
) {
    var expandedPriority by remember { mutableStateOf(false) }
    var expandedComplexity by remember { mutableStateOf(false) }

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
                    value = selectedPriority.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Task Priority") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedPriority = true },
                    trailingIcon = {
                        IconButton(onClick = { expandedPriority = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Task Priority")
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
                                Text(text = priority.name)
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
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                OutlinedTextField(
                    value = selectedComplexity.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Task Complexity") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedComplexity = true }
                )
                IconButton(onClick = { expandedComplexity = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Task Complexity")
                }
                DropdownMenu(expanded = expandedComplexity, onDismissRequest = { expandedComplexity = false }) {
                    // Iterate through the enum values to create dropdown items
                    ScrumboardConstants.Complexity.entries.forEach { complexity ->
                        DropdownMenuItem(text = {
                            Text(text = complexity.name)
                        },
                            onClick = {
                                onComplexityChange(complexity)
                                expandedComplexity = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    createTaskFn(title, description, selectedPriority, selectedComplexity)
                    onTitleChange("")
                    onDescriptionChange("")
                    navController.navigate("AllStories")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Create a task")
            }
        }
    }
}