package nz.ac.canterbury.seng303.scrumboardmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.scrumboardmobile.screens.CreateStoryScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.RegisterUserScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.UserList
import nz.ac.canterbury.seng303.scrumboardmobile.screens.ViewAllStories
import nz.ac.canterbury.seng303.scrumboardmobile.screens.ViewStoryScreen
import nz.ac.canterbury.seng303.scrumboardmobile.ui.theme.ScrumBoardTheme
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.common.AppBarViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.CreateStoryViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.CreateUserViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by koinViewModel()
    private val storyViewModel: StoryViewModel by koinViewModel()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScrumBoardTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val appBarViewModel: AppBarViewModel = viewModel()
                appBarViewModel.init()
                Scaffold(
                    topBar = {
                        // Add your AppBar content here
                        TopAppBar(
                            title = {
                                navBackStackEntry?.destination?.route?.let { route ->
                                    appBarViewModel.getNameById(route)?.run {
                                        Text(this)
                                    }
                                }
                            },
                            navigationIcon = {
                                if (navBackStackEntry?.destination?.route != "Home") {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            }
                        )

                    }
                ) {

                    Box(modifier = Modifier.padding(it)) {
                        val createUserViewModel: CreateUserViewModel = viewModel()
                        val createStoryViewModel: CreateStoryViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
                            composable("AllUsers") {
                                UserList(navController = navController, userViewModel = userViewModel)
                            }
                            composable("Register") {
                                RegisterUserScreen(
                                    navController = navController,
                                    username = createUserViewModel.username,
                                    onUsernameChange = { newUsername ->
                                        createUserViewModel.updateUsername(newUsername)
                                    },
                                    password = createUserViewModel.password,
                                    onPasswordChange = { newPassword ->
                                        createUserViewModel.updatePassword(newPassword)
                                    },
                                    firstName = createUserViewModel.firstName,
                                    onFirstNameChange = { newFirstName ->
                                        createUserViewModel.updateFirstName(newFirstName)
                                    },
                                    lastName = createUserViewModel.lastName,
                                    onLastNameChange = { newLastName ->
                                        createUserViewModel.updateLastName(newLastName)
                                    },
                                    createUserFn = { username, password, firstName, lastName ->
                                        userViewModel.createUser(
                                            username,
                                            password,
                                            firstName,
                                            lastName
                                        )
                                    }
                                )
                            }

                            composable("AllStories") {
                                ViewAllStories(navController = navController, storyViewModel = storyViewModel)
                            }

                            composable("CreateStory") {
                                CreateStoryScreen(
                                    navController = navController,
                                    title = createStoryViewModel.title,
                                    onTitleChange = { newTitle ->
                                        createStoryViewModel.updateTitle(newTitle)
                                    },
                                    description = createStoryViewModel.description,
                                    onDescriptionChange = { newDescription ->
                                        createStoryViewModel.updateDescription(newDescription)
                                    },
                                    createStoryFn = { title, description,timeCreated  ->
                                        storyViewModel.createStory(
                                            title,
                                            description,
                                            timeCreated
                                        )
                                    }
                                )
                            }
                            composable("Story/{storyId}", arguments = listOf(navArgument("storyId") {
                                type = NavType.StringType
                            })) { backStackEntry ->
                                val storyId = backStackEntry.arguments?.getString("storyId")
                                storyId?.let { storyIdParam: String ->
                                    ViewStoryScreen(navController = navController, storyId = storyIdParam, storyViewModel = storyViewModel)
                                }

                            }

                            composable("Story/{storyId}/CreateTask", arguments = listOf(navArgument("storyId") {

                            })) { backStackEntry ->
                                val storyId = backStackEntry.arguments?.getString("storyId")
                                storyId?.let { storyIdParam: String ->
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to ScrumBoard")
        Button(onClick = { navController.navigate("AllUsers") }) {
            Text("View Users")
        }
        Button(onClick = { navController.navigate("Register") }) {
            Text("Register")
        }
        Button(onClick = { navController.navigate("AllStories") }) {
            Text("View Stories")
        }
        Button(onClick = { navController.navigate("CreateStory") }) {
            Text("Create a Story")
        }
    }
}
