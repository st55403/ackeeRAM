package eu.golovkov.ackeeram.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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

@Composable
fun RAMBottomBar(
    navController: NavController,
) {
    val currentDestination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    // TODO: make it look like in FIGMA
    NavigationBar(
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
            )
        }
    }
}
