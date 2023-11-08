package eu.golovkov.ackeeram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import eu.golovkov.ackeeram.screens.NavGraphs
import eu.golovkov.ackeeram.screens.appCurrentDestinationAsState
import eu.golovkov.ackeeram.ui.RAMBottomBar
import eu.golovkov.ackeeram.ui.theme.AckeeRAMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AckeeRAMTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun App() {
    val engine = rememberAnimatedNavHostEngine()
    val navController = engine.rememberNavController()
    val currentDestination by navController.appCurrentDestinationAsState()
    // TODO: need to check this IMO character detail screen should not display bottom bar
    val showBottomBarWithFab = BottomBarDestination.containDestination(currentDestination)

    Scaffold(
        bottomBar = {
            if (showBottomBarWithFab) {
                RAMBottomBar(
                    navController = navController,
                )
            }
        },
    ) { paddingValues ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            engine = engine,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    AckeeRAMTheme {
        App()
    }
}
