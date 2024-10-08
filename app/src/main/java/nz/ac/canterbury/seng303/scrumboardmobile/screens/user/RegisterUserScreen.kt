package nz.ac.canterbury.seng303.scrumboardmobile.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.R

@Composable
fun RegisterUserScreen(
    navController: NavController,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    createUserFn: (String, String, String, String) -> Unit,
    grantAuthentication: suspend () -> Unit ) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(ContextCompat.getString(context, R.string.register_header))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                label = { Text(ContextCompat.getString(context, R.string.username)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text(ContextCompat.getString(context, R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { onFirstNameChange(it) },
                label = { Text(ContextCompat.getString(context, R.string.first_name)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { onLastNameChange(it) },
                label = { Text(ContextCompat.getString(context, R.string.last_name)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    createUserFn(username, password, firstName, lastName)
                    onUsernameChange("")
                    onPasswordChange("")
                    onFirstNameChange("")
                    onLastNameChange("")
                    CoroutineScope(Dispatchers.IO).launch {
                        grantAuthentication()
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text(ContextCompat.getString(context, R.string.register_label))
            }
        }
    }
}