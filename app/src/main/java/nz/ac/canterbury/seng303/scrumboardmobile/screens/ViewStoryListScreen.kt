package nz.ac.canterbury.seng303.scrumboardmobile.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel



@Composable
fun ViewAllStories(navController: NavController, storyViewModel: StoryViewModel) {
    storyViewModel.getStories()
    val stories: List<Story> by storyViewModel.stories.collectAsState(emptyList())
    LazyColumn {
        items(stories) { story ->
            StoryCard(navController = navController, storyViewModel = storyViewModel, storyId = story.storyId)
        }
    }
}

@Composable
fun StoryCard(
    navController: NavController,
    storyViewModel: StoryViewModel,
    storyId: Int
) {
    storyViewModel.getStoryWithTasks(storyId = storyId)
    val selectedStoryState by storyViewModel.selectedStoryWithTasks.collectAsState(null)
    val storyWithTasks: StoryWithTasks? = selectedStoryState
    val context = LocalContext.current
    if (storyWithTasks != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navController.navigate("Story/${storyId}")
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )

        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()

            ) {
                TaskStatusOverview(tasks = storyWithTasks.tasks)
                Text(
                    text = storyWithTasks.story.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = storyWithTasks.story.description,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


}

@Composable
fun TaskStatusOverview(tasks: List<Task>) {
    Row{
        val tasksInToDo =
            tasks.count { t -> t.status == ScrumboardConstants.Status.TO_DO }
        val tasksInProgress =
            tasks.count { t -> t.status == ScrumboardConstants.Status.IN_PROGRESS }
        val tasksInReview =
            tasks.count { t -> t.status == ScrumboardConstants.Status.UNDER_REVIEW }
        val tasksInDone =
            tasks.count { t -> t.status == ScrumboardConstants.Status.DONE }
        Text(
            text = "To Do: $tasksInToDo",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "In Progress: $tasksInProgress",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "In Review: $tasksInReview",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Done: $tasksInDone",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
    }
}