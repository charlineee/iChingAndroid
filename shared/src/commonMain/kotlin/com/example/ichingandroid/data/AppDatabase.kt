package com.example.ichingandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor

@Database(entities = [ReadingEntity::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return provideDatabaseBuilder()
}

expect fun provideDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
