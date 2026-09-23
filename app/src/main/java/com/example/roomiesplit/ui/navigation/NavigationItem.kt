package com.example.roomiesplit.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
    BILLS("Bills", Icons.Filled.Receipt, Icons.Outlined.Receipt, "nav_bills"),
    FUN_ZONE("Fun Zone", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports, "nav_fun_zone"),
    CHAT("Chat", Icons.Filled.Chat, Icons.Outlined.Chat, "nav_chat")
}
