package nz.ac.canterbury.seng303.scrumboardmobile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreateStoryScreen (
    navController: NavController,

    title: String,
    onTitleChange: (String) -> Unit,

    description: String,
    onDescriptionChange: (String) -> Unit,

    createStoryFn: (String, String, Long) -> Unit
) {

        val timeCreated = remember { mutableLongStateOf(0L) }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create a Story")
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { onTitleChange(it) },
                    label = { Text("Story Title") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { onDescriptionChange(it) },
                    label = { Text("Story Description") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        timeCreated.value = System.currentTimeMillis()
                        createStoryFn(title, description, timeCreated.value)
                        onTitleChange("")
                        onDescriptionChange("")
                        navController.navigate("AllStories")
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) {
                    Text("Create")
                }
            }

        }
}