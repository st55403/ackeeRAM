package eu.golovkov.ackeeram.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import eu.golovkov.ackeeram.BottomBarDestination
import eu.golovkov.ackeeram.screens.NavGraphs
import eu.golovkov.ackeeram.screens.appCurrentDestinationAsState
import eu.golovkov.ackeeram.screens.startAppDestination
import eu.golovkov.ackeeram.ui.theme.RAMColor

@Composable
fun RAMBottomBar(
    navController: NavController,
) {
    val currentDestination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isSystemInDarkTheme) {
        RAMColor.backgroundsBottomNavDm
    } else {
        RAMColor.backgroundsBottomNav
    }
    val selectedColor = if (isSystemInDarkTheme) {
        RAMColor.iconsTertiaryDm
    } else {
        RAMColor.iconsTertiary
    }
    NavigationBar(
        containerColor = backgroundColor
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val isSelected = currentDestination.route == destination.direction.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.direction) {
                        popUpTo(NavGraphs.root.route) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = stringResource(destination.titleRes),
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.titleRes),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    indicatorColor = backgroundColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = RAMColor.iconsSecondary,
                    unselectedTextColor = RAMColor.iconsSecondary,
                )
            )
        }
    }
}
