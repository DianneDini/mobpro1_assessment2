package org.d3if0108.mobpro1_assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0108.mobpro1_assessment2.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(catatan: User)

    @Update
    suspend fun update(catatan: User)

    @Query("SELECT * FROM user ORDER BY tanggal DESC")
    fun getCatatan(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getCatatanById(id: Long): User?

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Long)
}