package com.mythly.app.data.local

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<MythlyDatabase>
