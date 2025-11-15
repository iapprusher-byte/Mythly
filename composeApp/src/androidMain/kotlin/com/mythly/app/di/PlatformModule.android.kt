package com.mythly.app.di

import com.mythly.app.data.local.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        getDatabaseBuilder(androidContext())
            .fallbackToDestructiveMigration(true)
            .build()
    }

    // Provide JSON content from Android assets
    single {
        androidContext().assets.open("dummy_stories.json")
            .bufferedReader()
            .use { it.readText() }
    }
}
