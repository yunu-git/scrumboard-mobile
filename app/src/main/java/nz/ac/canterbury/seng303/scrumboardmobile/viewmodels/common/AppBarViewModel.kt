package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.common

import android.content.Context
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.R

class AppBarViewModel: ViewModel() {
    private val idNameMap: HashMap<String, Int> = HashMap()

    fun getNameById(id: String, context: Context): String? {
        return idNameMap[id]?.let { context.getString(it) }
    }

    fun init() {
        idNameMap["Home"] = R.string.app_name
        idNameMap["AllUsers"] = -1
        idNameMap["CreateStory"] = R.string.create_story_label
        idNameMap["AllStories"] = R.string.stories
        idNameMap["Register"] = R.string.register_label
        idNameMap["Login"] = R.string.login_label
    }
}
