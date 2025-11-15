package com.mythly.app.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Database - provided by platform module
    // DAOs
    single { get<com.mythly.app.data.local.MythlyDatabase>().storyDao() }
    single { get<com.mythly.app.data.local.MythlyDatabase>().userStatsDao() }
    single { get<com.mythly.app.data.local.MythlyDatabase>().readingSessionDao() }
}

// Complete modules list
fun getAppModules() = listOf(
    platformModule,
    sharedModule,
    dataModule,
    domainModule
)
