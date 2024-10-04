package nz.ac.canterbury.seng303.scrumboardmobile.screens


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel



@Composable
fun ViewAllStories(navController: NavController, storyViewModel: StoryViewModel) {
    storyViewModel.getStories()
    val Stories: List<Story> by storyViewModel.stories.collectAsState(emptyList())
    LazyColumn {
        items(Stories) { story ->
            Text(
                text = story.title,
            )
            Text(
                text = story.description,
            )
            Text(
                text = story.timeCreated.toString(),
            )
            Text(
                text = story.storyId.toString()
            )
        }
    }
}