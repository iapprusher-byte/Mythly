package com.mythly.app.di

import com.mythly.app.presentation.viewmodel.LibraryViewModel
import com.mythly.app.presentation.viewmodel.ProfileViewModel
import com.mythly.app.presentation.viewmodel.StoryReaderViewModel
import com.mythly.app.presentation.viewmodel.TodayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModels
    viewModel { TodayViewModel(get(), get(), get()) }
    viewModel { LibraryViewModel(get(), get(), get()) }
    viewModel { StoryReaderViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}
