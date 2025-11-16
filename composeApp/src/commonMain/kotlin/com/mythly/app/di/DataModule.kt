package com.mythly.app.di

import com.mythly.app.data.repository.StoryRepositoryImpl
import com.mythly.app.data.repository.UserRepositoryImpl
import com.mythly.app.domain.repository.StoryRepository
import com.mythly.app.domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<StoryRepository> {
        StoryRepositoryImpl(storyDao = get())
    }

    single<UserRepository> {
        UserRepositoryImpl(
            userStatsDao = get(),
            readingSessionDao = get()
        )
    }
}
