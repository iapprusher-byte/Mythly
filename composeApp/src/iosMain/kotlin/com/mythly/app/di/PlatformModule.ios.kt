package com.mythly.app.di

import com.mythly.app.data.local.getDatabaseBuilder
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual val platformModule = module {
    single {
        getDatabaseBuilder(Unit)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    // Provide JSON content from iOS bundle resources
    single {
        val bundle = NSBundle.mainBundle
        val path = bundle.pathForResource("dummy_stories", ofType = "json")
            ?: throw IllegalStateException("dummy_stories.json not found in bundle")

        NSString.stringWithContentsOfFile(
            path = path,
            encoding = NSUTF8StringEncoding,
            error = null
        ) ?: throw IllegalStateException("Failed to read dummy_stories.json")
    }
}
