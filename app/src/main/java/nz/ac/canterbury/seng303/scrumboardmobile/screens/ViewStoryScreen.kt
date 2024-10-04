package nz.ac.canterbury.seng303.scrumboardmobile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel

@Composable
fun ViewStoryScreen(
    storyId: String,
    navController: NavController,
    storyViewModel: StoryViewModel,
) {
    val context = LocalContext.current
    storyViewModel.getStoryWithTasks(storyId = storyId.toIntOrNull())
    val selectedStoryState by storyViewModel.selectedStoryWithTasks.collectAsState(null)
    val storyWithTasks: StoryWithTasks? = selectedStoryState
    var fabStateExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    if (storyWithTasks != null) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("Story/$storyId/CreateTask") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {


            }
        }
    }
}
