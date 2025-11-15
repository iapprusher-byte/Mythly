package com.mythly.app

import androidx.compose.ui.window.ComposeUIViewController
import com.mythly.app.di.getAppModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

private var koinInitialized = false

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS only once
    if (!koinInitialized) {
        startKoin {
            modules(getAppModules())
        }
        koinInitialized = true
    }

    App()
}
