package nz.ac.canterbury.seng303.scrumboardmobile.screens.user

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserLoginModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel

@Composable
fun LoginUserScreen(
    userViewModel: UserViewModel,
    navController: NavController,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    grantAuthentication: suspend () -> Unit )
{
    val context = LocalContext.current
    val isAuth by userViewModel.isAuthenticated.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    userViewModel.authenticateUser(username, password)
                    if (isAuth) {
                        navController.popBackStack()
                        CoroutineScope(Dispatchers.IO).launch {
                            grantAuthentication()
                        }
                    } else {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Login")
            }
        }
    }

}