package nz.ac.canterbury.seng303.scrumboardmobile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants


@Composable
fun ViewStoryScreen(
    storyId: String,
    navController: NavController,
    storyViewModel: StoryViewModel,
) {
    storyViewModel.getStoryWithTasks(storyId = storyId.toIntOrNull())
    val selectedStoryState by storyViewModel.selectedStoryWithTasks.collectAsState(null)
    val storyWithTasks: StoryWithTasks? = selectedStoryState

    if (storyWithTasks != null) {
        Scaffold(
            floatingActionButton = {
                ExtendedCreateTaskFab(navController = navController, storyId = storyId)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                storyWithTasks.let { story ->
                    Text("Story Title: ${story.story.title}")
                    Text("Description: ${story.story.description}")
                }

            ScrollableStatusCards(storyWithTasks)
        }
    }
}
@Composable
fun ScrollableStatusCards(storyWithTasks: StoryWithTasks?) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var currentStatusIndex by remember { mutableStateOf(0) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ScrumboardConstants.Status.values().forEachIndexed { index, status ->
                Button(
                    onClick = {
                        currentStatusIndex = index
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStatusIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(status.name)
                }
            }
        }

        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(ScrumboardConstants.Status.values()) { index, status ->
                Card(
                    modifier = Modifier
                        .fillParentMaxHeight(0.7f)
                        .fillParentMaxWidth(0.9f)
                        .padding(8.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(status.name, style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            storyWithTasks?.tasks?.filter { it.status == status }?.forEach { task ->
                                Text(task.title, style = MaterialTheme.typography.bodyMedium)
                            }
                            if (storyWithTasks?.tasks?.none { it.status == status } == true) {
                                Text("No tasks in this status", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                currentStatusIndex = index
            }
    }
}
@Composable
fun ExtendedCreateTaskFab(navController: NavController, storyId: String) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate("Story/$storyId/CreateTask") },
        text = { Text(text = "Add Task") },
        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task") }
    )
}
