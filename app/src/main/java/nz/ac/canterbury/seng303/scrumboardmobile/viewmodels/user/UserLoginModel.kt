package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserLoginModel(): ViewModel() {
    var loginUsername by mutableStateOf("")
        private set
    fun updateUsername(newUsername: String) {
        loginUsername = newUsername
    }
    var loginPassword by mutableStateOf("")
    fun updatePassword(newPassword: String) {
        loginPassword = newPassword
    }

}
