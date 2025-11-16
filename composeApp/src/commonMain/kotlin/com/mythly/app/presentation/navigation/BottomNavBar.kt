package com.mythly.app.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.Book
import compose.icons.feathericons.User

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = "today",
        icon = FeatherIcons.Home,
        label = "Today"
    ),
    BottomNavItem(
        route = "library",
        icon = FeatherIcons.Book,
        label = "Library"
    ),
    BottomNavItem(
        route = "profile",
        icon = FeatherIcons.User,
        label = "Profile"
    )
)

@Composable
fun MythlyBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
