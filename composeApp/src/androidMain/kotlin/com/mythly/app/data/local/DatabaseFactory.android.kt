package com.mythly.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.datetime.Clock

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<MythlyDatabase> {
    val appContext = (context as Context).applicationContext
    val dbFile = appContext.getDatabasePath("mythly.db")
    return Room.databaseBuilder(
        context = appContext,
        klass = MythlyDatabase::class.java,
        name = dbFile.absolutePath
    )
}
