package com.mythly.app.di

import com.mythly.app.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    factory { GetTodayStoryUseCase(get()) }
    factory { GetAllStoriesUseCase(get()) }
    factory { SearchStoriesUseCase(get()) }
    factory { FilterStoriesByDeityUseCase(get()) }
    factory { FilterStoriesByEpicUseCase(get()) }
    factory { MarkStoryReadUseCase(get(), get()) }
    factory { GetUserStatsUseCase(get()) }
    factory { LoadInitialContentUseCase(get()) }
}
