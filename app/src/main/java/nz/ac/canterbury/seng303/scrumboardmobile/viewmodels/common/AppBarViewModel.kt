package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.common

import androidx.lifecycle.ViewModel

class AppBarViewModel: ViewModel() {
    private val idNameMap: HashMap<String, String> = HashMap()

    fun getNameById(id: String): String? {
        return idNameMap[id]
    }

    fun init() {
        idNameMap["Home"] = "ScrumBoard Mobile"
        idNameMap["AllUsers"] = "All Users"
        idNameMap["CreateStory"] = "Create a Story"
        idNameMap["AllStories"] = "Stories"
        idNameMap["Register"] = "Register"
    }
}
