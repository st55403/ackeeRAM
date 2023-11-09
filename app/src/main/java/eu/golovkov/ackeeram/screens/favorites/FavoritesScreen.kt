package eu.golovkov.ackeeram.screens.favorites

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import eu.golovkov.ackeeram.R
import eu.golovkov.ackeeram.Samples
import eu.golovkov.ackeeram.StatefulLayout
import eu.golovkov.ackeeram.screens.characters.CharacterItem
import eu.golovkov.ackeeram.screens.destinations.CharacterDetailsScreenDestination
import eu.golovkov.ackeeram.ui.theme.AckeeRAMTheme
import eu.golovkov.ackeeram.ui.theme.RAMPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Destination()
@Composable
fun FavoritesScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel: FavoritesViewModel = viewModel()

    Favorites(
        stateHolder = viewModel,
        onCharacterClick = { characterId ->
            navigator.navigate(CharacterDetailsScreenDestination(characterId))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Favorites(
    stateHolder: FavoritesStateHolder,
    onCharacterClick: (Int) -> Unit = {},
) {
    val state = stateHolder.state.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorites_title),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    FavoritesStateHolder.State.Message.Empty -> {
                        Text(
                            text = stringResource(R.string.favorites_empty_message)
                        )
                    }

                    is FavoritesStateHolder.State.Message.Error -> {
                        Text(
                            text = message.message ?: stringResource(R.string.generic_error_message)
                        )
                    }
                }
            },
            modifier = Modifier.padding(it)
        ) {
            val isDarkTheme = isSystemInDarkTheme()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(RAMPadding.small)
            ) {
                items(it.characters) { character ->
                    CharacterItem(
                        character = character,
                        isDarkTheme = isDarkTheme,
                        onClick = { onCharacterClick(character.id) }
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesPreview() {
    AckeeRAMTheme {
        Favorites(
            stateHolder = object : FavoritesStateHolder {
                override val state: StateFlow<FavoritesStateHolder.State> =
                    MutableStateFlow(
                        FavoritesStateHolder.State.Data(
                            characters = Samples.characters
                        )
                    )
            }
        )
    }
}
