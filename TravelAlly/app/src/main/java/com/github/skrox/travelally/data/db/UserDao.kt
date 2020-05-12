package com.github.skrox.travelally.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.skrox.travelally.data.db.entities.CURRENT_USER_ID
import com.github.skrox.travelally.data.db.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User)

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>

    @Delete
    fun deleteUser(user: User)
}