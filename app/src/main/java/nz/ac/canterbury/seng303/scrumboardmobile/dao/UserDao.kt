package nz.ac.canterbury.seng303.scrumboardmobile.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng303.scrumboardmobile.models.User

@Dao
interface UserDao {
    @Insert(entity = User::class)
    suspend fun insertUser(vararg user: User)
    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>
    @Query("SELECT * FROM User WHERE username = :username")
    suspend
    fun findByUsername(username: String): User?
}