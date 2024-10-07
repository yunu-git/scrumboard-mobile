package nz.ac.canterbury.seng303.scrumboardmobile.screens.story

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.StoryWithTasks
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task


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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = storyWithTasks.story.title,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = storyWithTasks.story.description,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start
                        )
                    }

                }


                ScrollableStatusCards(navController = navController, storyWithTasks = storyWithTasks)
            }
        }
    }
}

@Composable
fun ScrollableStatusCards(navController: NavController, storyWithTasks: StoryWithTasks) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var currentStatusIndex by remember { mutableIntStateOf(0) }
    val cardWidth = remember { mutableStateOf(0) }

    Column {
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(ScrumboardConstants.Status.entries) { index, status ->
                ElevatedCard(
                    modifier = Modifier
                        .fillParentMaxHeight(0.7f)
                        .fillParentMaxWidth(0.9f)
                        .padding(8.dp)
                        .onGloballyPositioned { coordinates ->
                            cardWidth.value = coordinates.size.width
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        TaskList(
                            navController = navController,
                            tasks =  storyWithTasks.tasks,
                            status = status
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PaginatedStatusButtons(
                currentStatusIndex = currentStatusIndex,
                onStatusChange = { newIndex ->
                    currentStatusIndex = newIndex
                    coroutineScope.launch {
                        listState.animateScrollToItem(newIndex)
                    }
                }
            )
        }
    }

    LaunchedEffect(listState, cardWidth.value) {
        snapshotFlow {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
            val halfCardWidth = cardWidth.value / 2

            when {
                firstVisibleItemScrollOffset > halfCardWidth -> firstVisibleItemIndex + 1
                firstVisibleItemScrollOffset < -halfCardWidth -> firstVisibleItemIndex - 1
                else -> firstVisibleItemIndex
            }
        }.collect { index ->
            currentStatusIndex = index.coerceIn(0, ScrumboardConstants.Status.entries.size - 1)
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

@Composable
fun PaginatedStatusButtons(
    currentStatusIndex: Int,
    onStatusChange: (Int) -> Unit
) {
    val statuses = ScrumboardConstants.Status.entries

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val previousIndex = (currentStatusIndex - 1 + statuses.size) % statuses.size
                onStatusChange(previousIndex)
            },
            enabled = currentStatusIndex > 0
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Status",
                tint = if (currentStatusIndex > 0) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)
            )
        }

        Text(
            text = statuses[currentStatusIndex].status,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = {
                val nextIndex = (currentStatusIndex + 1) % statuses.size
                onStatusChange(nextIndex)
            },
            enabled = currentStatusIndex < statuses.size - 1
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Status",
                tint = if (currentStatusIndex < statuses.size - 1) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)

            )
        }
    }
}

@Composable
fun TaskList(
    navController: NavController,
    status: ScrumboardConstants.Status,
    tasks: List<Task>
) {
    val filteredTasks = tasks.filter { it.status == status }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = status.status,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start
        )
        if (filteredTasks.isNotEmpty()) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                tasks.filter { it.status == status }.forEach { task ->
                    TaskCard(navController = navController, task = task)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No tasks in this status.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

@Composable
fun TaskCard(
    navController: NavController,
    task: Task
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("Story/${task.storyId}/Task/${task.taskId}")
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(task.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}