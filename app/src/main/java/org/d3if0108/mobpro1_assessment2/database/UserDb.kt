package org.d3if0108.mobpro1_assessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0108.mobpro1_assessment2.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDb : RoomDatabase() {

    abstract val dao: UserDao

    companion object {

        @Volatile
        private var INSTANCE: UserDb? = null

        fun getInstance(context: Context): UserDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java,
                        "catatan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}