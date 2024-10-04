package nz.ac.canterbury.seng303.scrumboardmobile.screens.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.scrumboardmobile.models.User
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel

@Composable
fun UserList(navController: NavController, userViewModel: UserViewModel) {
    userViewModel.getUsers()
    val users: List<User> by userViewModel.users.collectAsState(emptyList())
    LazyColumn {
        items(users) { user ->
            Text(
                text = user.firstName
            )
        }
    }
}