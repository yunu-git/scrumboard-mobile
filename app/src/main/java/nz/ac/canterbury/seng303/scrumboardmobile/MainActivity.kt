package nz.ac.canterbury.seng303.scrumboardmobile

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.screens.story.CreateStoryScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.task.CreateTaskScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.user.RegisterUserScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.story.ViewAllStories
import nz.ac.canterbury.seng303.scrumboardmobile.screens.story.ViewStoryScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.task.ViewTaskScreen
import nz.ac.canterbury.seng303.scrumboardmobile.screens.user.LoginUserScreen
import nz.ac.canterbury.seng303.scrumboardmobile.ui.theme.ScrumBoardTheme
import nz.ac.canterbury.seng303.scrumboardmobile.util.hashPassword
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.common.AppBarViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.CreateStoryViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task.CreateTaskViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task.TaskViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.CreateUserViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserLoginModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by koinViewModel()
    private val storyViewModel: StoryViewModel by koinViewModel()
    private val taskViewModel: TaskViewModel by koinViewModel()
    private val AUTHENTICATION = booleanPreferencesKey("authentication")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")



    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAuth: Flow<Boolean> = dataStore.data
            .map { preferences ->
                preferences[AUTHENTICATION] ?: false
            }


        suspend fun grantAuthentication() {
            dataStore.edit { settings ->
                settings[AUTHENTICATION] = true
            }
        }

        suspend fun removeAuthentication() {
            dataStore.edit { settings ->
                settings[AUTHENTICATION] = false
            }
        }

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
                        val createTaskViewModel: CreateTaskViewModel = viewModel()
                        val userLoginModel: UserLoginModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController,
                                    isAuth = isAuth,
                                    removeAuthenticationFn = { removeAuthentication()})
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
                                            hashPassword(password),
                                            firstName,
                                            lastName
                                        )
                                    },
                                    grantAuthentication = { grantAuthentication() }
                                )
                            }
                            composable("Login") {
                                LoginUserScreen(
                                    userViewModel = userViewModel,
                                    navController = navController,
                                    username = userLoginModel.loginUsername,
                                    onUsernameChange = { newName ->
                                        userLoginModel.updateUsername(newName)
                                    },
                                    password = userLoginModel.loginPassword,
                                    onPasswordChange = { newPassword ->
                                        userLoginModel.updatePassword(newPassword)
                                    },
                                    grantAuthentication = { grantAuthentication() }
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

                            composable(
                                route = "Story/{storyId}/CreateTask",
                                arguments = listOf(navArgument("storyId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val storyId = backStackEntry.arguments?.getString("storyId")

                                storyId?.let { storyIdParam: String ->
                                    CreateTaskScreen(
                                        navController = navController,
                                        title = createTaskViewModel.title,
                                        onTitleChange = { newTitle ->
                                            createTaskViewModel.updateTitle(newTitle)
                                        },
                                        description = createTaskViewModel.description,

                     onDescriptionChange = { newDescription ->
                                            createTaskViewModel.updateDescription(newDescription)
                                        },
                                        estimate = createTaskViewModel.estimate,
                                        onEstimateChange = { newEstimate ->
                                            createTaskViewModel.updateEstimate(newEstimate)
                                        },
                                        selectedPriority = createTaskViewModel.priority,
                                        onPriorityChange = { newPriority ->
                                            createTaskViewModel.updatePriority(newPriority)
                                        },
                                        selectedComplexity = createTaskViewModel.complexity,
                                        onComplexityChange = { newComplexity ->
                                            createTaskViewModel.updateComplexity(newComplexity)
                                        },
                                        createTaskFn = { title, description, estimate, selectedPriority, selectedComplexity ->
                                            taskViewModel.createTask(
                                                title,
                                                description,
                                                selectedComplexity,
                                                selectedPriority,
                                                estimate,
                                                storyIdParam.toInt()
                                            )
                                        }
                                    )
                                }
                            }
                            composable(
                                "Story/{storyId}/Task/{taskId}",
                                arguments = listOf(
                                    navArgument("storyId") { type = NavType.StringType },
                                    navArgument("taskId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val storyId = backStackEntry.arguments?.getString("storyId")
                                val taskId = backStackEntry.arguments?.getString("taskId")

                                if (storyId != null && taskId != null) {
                                    ViewTaskScreen(
                                        navController = navController,
                                        taskViewModel = taskViewModel,
                                        storyId = storyId,
                                        taskId = taskId
                                    )
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
fun Home(navController: NavController,
         isAuth: Flow<Boolean>,
         removeAuthenticationFn: suspend () -> Unit
         ) {
    val isAuthenticated by isAuth.collectAsState(initial = false)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to ScrumBoard")
        if (!isAuthenticated) {
            Button(onClick = { navController.navigate("Register") }) {
                Text("Register")
            }

            Button(onClick = { navController.navigate("Login") }) {
                Text("Log in")
            }
        } else {
            Button(onClick = { navController.navigate("AllStories") }) {
                Text("View Stories")
            }
            Button(onClick = { navController.navigate("CreateStory") }) {
                Text("Create a Story")
            }
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    removeAuthenticationFn()
                }
            }) {
                Text(text = "Log out")
            }
        }
    }
}
