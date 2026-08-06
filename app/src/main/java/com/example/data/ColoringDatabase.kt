package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedPageEntity::class, FavoriteSwatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ColoringDatabase : RoomDatabase() {
    abstract fun coloringDao(): ColoringDao

    companion object {
        @Volatile
        private var INSTANCE: ColoringDatabase? = null

        fun getInstance(context: Context): ColoringDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColoringDatabase::class.java,
                    "coloring_book_db"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
