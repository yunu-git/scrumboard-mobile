package nz.ac.canterbury.seng303.scrumboardmobile.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel



@Composable
fun ViewAllStories(navController: NavController, storyViewModel: StoryViewModel) {
    storyViewModel.getStories()
    val storiesWithTasks: List<StoryWithTasks> by storyViewModel.storiesWithTasks.collectAsState(emptyList())
    if (storiesWithTasks.isNotEmpty()) {
        Scaffold(
            floatingActionButton = {
                ExtendedCreateStoryFab(navController = navController)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                LazyColumn {
                    items(storiesWithTasks) { story ->
                        StoryCard(navController = navController, storyWithTasks = story)
                    }
                }

            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No stories available.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Text(
                    text = "You can create some here.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Button(
                    onClick = {navController.navigate("CreateStory")}
                ) {
                    Text(text = "Create a Story")
                }
            }
        }
    }
}


@Composable
fun StoryCard(
    navController: NavController,
    storyWithTasks: StoryWithTasks?
) {
    if (storyWithTasks != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navController.navigate("Story/${storyWithTasks.story.storyId}")
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column {
                TaskStatusBar(tasks = storyWithTasks.tasks)
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    TaskStatusText(tasks = storyWithTasks.tasks)
                    Divider()
                    Text(
                        text = storyWithTasks.story.title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = storyWithTasks.story.description,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun TaskStatusBar(tasks: List<Task>) {
    val tasksInProgress = tasks.count { it.status == ScrumboardConstants.Status.IN_PROGRESS }
    val tasksInReview = tasks.count { it.status == ScrumboardConstants.Status.UNDER_REVIEW }
    val tasksInDone = tasks.count { it.status == ScrumboardConstants.Status.DONE }
    val totalTasks = tasks.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (totalTasks > 0) {
                if (tasksInProgress > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(tasksInProgress.toFloat())
                            .background(Color.Blue)
                    )
                }
                if (tasksInReview > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(tasksInReview.toFloat())
                            .background(Color.Yellow)
                    )
                }
                if (tasksInDone > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(tasksInDone.toFloat())
                            .background(Color.Green)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskStatusText(tasks: List<Task>) {
    val tasksInToDo = tasks.count { it.status == ScrumboardConstants.Status.TO_DO }
    val tasksInProgress = tasks.count { it.status == ScrumboardConstants.Status.IN_PROGRESS }
    val tasksInReview = tasks.count { it.status == ScrumboardConstants.Status.UNDER_REVIEW }
    val tasksInDone = tasks.count { it.status == ScrumboardConstants.Status.DONE }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusText("To Do: $tasksInToDo")
        StatusText("In Progress: $tasksInProgress")
        StatusText("In Review: $tasksInReview")
        StatusText("Done: $tasksInDone")
    }
}

@Composable
private fun StatusText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ExtendedCreateStoryFab(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate("CreateStory") },
        text = { Text(text = "Add a Story") },
        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add a Story") }
    )
}