package eu.golovkov.ackeeram.screens.characters

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import eu.golovkov.ackeeram.R
import eu.golovkov.ackeeram.StatefulLayout
import eu.golovkov.ackeeram.asData
import eu.golovkov.ackeeram.model.CharacterRAM
import eu.golovkov.ackeeram.ui.theme.AckeeRAMTheme
import eu.golovkov.ackeeram.ui.theme.RAMColor
import eu.golovkov.ackeeram.ui.theme.RAMPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@RootNavGraph(start = true)
@Destination()
@Composable
fun CharactersScreen() {
    val viewModel: CharactersViewModel = viewModel()
    Characters(
        stateHolder = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Characters(
    stateHolder: CharactersStateHolder,
) {
    val state = stateHolder.state.collectAsState().value
    val transactions = state.asData()?.characters?.collectAsLazyPagingItems() ?: return

    LaunchedEffect(transactions.loadState, transactions.itemCount) {
        stateHolder.onLoadStatesChanged(transactions.loadState, transactions.itemCount)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.characters_title),
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
                    is CharactersStateHolder.State.Message.Empty -> {
                        Text(
                            text = stringResource(R.string.characters_empty_result),
                        )
                    }

                    is CharactersStateHolder.State.Message.Error -> {
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
                for (index in 0 until transactions.itemCount) {
                    when (transactions.peek(index)) {
                        is CharacterRAM -> {
                            item {
                                CharacterItem(
                                    character = transactions[index] as CharacterRAM,
                                    isDarkTheme = isDarkTheme,
                                )
                            }
                        }
                    }
                }

                if (transactions.loadState.refresh is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            CircularProgressIndicator(
                                Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: CharacterRAM,
    isDarkTheme: Boolean,
) {
    val backgroundColor = if (isDarkTheme) {
        RAMColor.backgroundsSecondaryDm
    } else {
        RAMColor.foregroundsPrimaryDm
    }
    val iconTintColor = if (isDarkTheme) {
        RAMColor.iconsTertiaryDm
    } else {
        RAMColor.iconsTertiary
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = RAMPadding.tiny)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(RAMPadding.small)
            )
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = null,
            modifier = modifier
                .padding(RAMPadding.tiny)
                .size(RAMPadding.large)
                .clip(RoundedCornerShape(RAMPadding.small)),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(RAMPadding.small),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = character.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.displayMedium
                )
                if (character.isFavorite) {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites),
                        contentDescription = null,
                        modifier = modifier
                            .padding(horizontal = RAMPadding.tiny)
                            .size(RAMPadding.medium),
                        tint = iconTintColor,
                    )
                }
            }
            Text(
                text = character.status,
                color = RAMColor.foregroundsSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharactersPreview() {
    AckeeRAMTheme {
        Characters(
            stateHolder = object : CharactersStateHolder {
                override val state: StateFlow<CharactersStateHolder.State> =
                    MutableStateFlow(
                        CharactersStateHolder.State.Data(
                            characters = flowOf(
                                PagingData.from(
                                    listOf(
                                        CharacterRAM(
                                            1,
                                            "Supercalifragilisticexpialidocious",
                                            "Active",
                                            "https://example.com/character1.png"
                                        ),
                                        CharacterRAM(
                                            2,
                                            "Hippopotomonstrosesquippedaliophobia",
                                            "Inactive",
                                            "https://example.com/character2.png"
                                        ),
                                        CharacterRAM(
                                            3,
                                            "Pneumonoultramicroscopicsilicovolcanoconiosis",
                                            "Active",
                                            "https://example.com/character3.png"
                                        ),
                                        CharacterRAM(
                                            4,
                                            "Floccinaucinihilipilification",
                                            "Active",
                                            "https://example.com/character4.png"
                                        ),
                                        CharacterRAM(
                                            5,
                                            "Antidisestablishmentarianism",
                                            "Inactive",
                                            "https://example.com/character5.png"
                                        )
                                    )
                                )
                            )
                        )
                    )
            }
        )
    }
}