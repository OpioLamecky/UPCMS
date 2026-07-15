package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        Officer::class,
        Case::class,
        Statement::class,
        Complainant::class,
        Suspect::class,
        Witness::class,
        Evidence::class,
        InvestigationLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PcmsDatabase : RoomDatabase() {

    abstract fun pcmsDao(): PcmsDao

    companion object {
        @Volatile
        private var INSTANCE: PcmsDatabase? = null

        fun getDatabase(context: Context): PcmsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PcmsDatabase::class.java,
                    "pcms_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
