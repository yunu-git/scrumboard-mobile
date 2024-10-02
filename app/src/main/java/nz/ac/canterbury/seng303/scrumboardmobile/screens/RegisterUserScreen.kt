package nz.ac.canterbury.seng303.scrumboardmobile.screens

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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
    createUserFn: (String, String, String, String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register for ScrumBoard Mobile")
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { onFirstNameChange(it) },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { onLastNameChange(it) },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    createUserFn(username, password, firstName, lastName)
                    onUsernameChange("")
                    onPasswordChange("")
                    onFirstNameChange("")
                    onLastNameChange("")
                    navController.navigate("AllUsers")
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Register")
            }
        }



    }
}