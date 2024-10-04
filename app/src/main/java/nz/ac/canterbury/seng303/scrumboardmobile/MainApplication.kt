package nz.ac.canterbury.seng303.scrumboardmobile

import android.app.Application
import androidx.room.Room
import nz.ac.canterbury.seng303.scrumboardmobile.datastore.Database
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story.StoryViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task.TaskViewModel
import nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.user.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "app_database"
        ).build()
    }

    single { get<Database>().storyDao() }
    single { get<Database>().taskDao() }
    single { get<Database>().workLogDao() }
    single { get<Database>().userDao() }

    // ViewModels
    viewModel { UserViewModel(get()) }
    viewModel { StoryViewModel(get()) }
    viewModel { TaskViewModel(get()) }
}