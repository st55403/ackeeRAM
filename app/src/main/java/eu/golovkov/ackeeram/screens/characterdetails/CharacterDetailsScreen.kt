package eu.golovkov.ackeeram.screens.characterdetails

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import eu.golovkov.ackeeram.R
import eu.golovkov.ackeeram.StatefulLayout
import eu.golovkov.ackeeram.asData
import eu.golovkov.ackeeram.model.CharacterRAMDerails
import eu.golovkov.ackeeram.model.Location
import eu.golovkov.ackeeram.model.Origin
import eu.golovkov.ackeeram.ui.theme.AckeeRAMTheme
import eu.golovkov.ackeeram.ui.theme.RAMColor
import eu.golovkov.ackeeram.ui.theme.RAMPadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Destination()
@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val viewModel: CharacterDetailsViewModel = viewModel(
        factory = CharacterDetailsViewModel.Factory(characterId)
    )

    val onBackClick = {
        resultNavigator.navigateBack(
            result = viewModel.state.value.asData()?.wasChanged ?: false,
        )
    }

    CharacterDetails(
        stateHolder = viewModel,
        onBackClick = onBackClick
    )

    BackHandler {
        onBackClick()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetails(
    stateHolder: CharacterDetailsStateHolder,
    onBackClick: () -> Unit = {},
) {
    val state = stateHolder.state.collectAsState().value
    val isDarkTheme = isSystemInDarkTheme()
    val name = state.asData()?.character?.name
    val isFavorite = state.asData()?.isFavorite

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (name.isNullOrEmpty()) {
                            stringResource(R.string.characters_details_title)
                        } else {
                            name
                        },
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null,
                            tint = RAMColor.foregroundsPrimary
                        )
                    }
                },
                actions = {
                    isFavorite?.let {
                        IconButton(
                            onClick = stateHolder::changeFavorite
                        ) {
                            val icon = if (isFavorite) {
                                R.drawable.ic_favorites
                            } else {
                                R.drawable.ic_favorite_empty
                            }
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                                tint = if (isFavorite) {
                                    RAMColor.accentPrimary
                                } else {
                                    RAMColor.foregroundsSecondary
                                }
                            )
                        }
                    }
                }
            )
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    is CharacterDetailsStateHolder.State.Message.Error -> {
                        Text(
                            text = message.message ?: stringResource(R.string.generic_error_message)
                        )
                    }
                }
            },
            modifier = Modifier.padding(it)
        ) {
            val character = state.asData()?.character ?: return@StatefulLayout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(RAMPadding.small)
                    .background(
                        color = if (isDarkTheme) {
                            RAMColor.backgroundsSecondaryDm
                        } else {
                            RAMColor.foregroundsPrimaryDm
                        },
                        shape = RoundedCornerShape(RAMPadding.small)
                    ),
                verticalArrangement = Arrangement.spacedBy(RAMPadding.medium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = RAMPadding.medium)
                        .padding(top = RAMPadding.medium)
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(RAMPadding.massive)
                            .clip(RoundedCornerShape(RAMPadding.small)),
                        contentScale = ContentScale.Crop,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(RAMPadding.medium)
                    ) {
                        CardItem(
                            label = R.string.characters_details_name_label,
                            value = character.name
                        )
                    }
                }
                Divider()
                CardItem(
                    label = R.string.characters_details_status_label,
                    value = character.status
                )
                CardItem(
                    label = R.string.characters_details_species_label,
                    value = character.species
                )
                CardItem(
                    label = R.string.characters_details_type_label,
                    value = character.type
                )
                CardItem(
                    label = R.string.characters_details_gender_label,
                    value = character.gender
                )
                CardItem(
                    label = R.string.characters_details_origin_label,
                    value = character.origin.name
                )
                CardItem(
                    label = R.string.characters_location_label,
                    value = character.location.name,
                    modifier = Modifier.padding(bottom = RAMPadding.medium)
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    label: Int,
    value: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = RAMPadding.medium)
    ) {
        Text(
            text = stringResource(label),
            color = RAMColor.foregroundsSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = if (value.isNullOrEmpty()) {
                stringResource(R.string.undefined)
            } else {
                value
            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsPreview() {
    AckeeRAMTheme {
        CharacterDetails(
            stateHolder = object : CharacterDetailsStateHolder {
                override val state: StateFlow<CharacterDetailsStateHolder.State> =
                    MutableStateFlow(
                        CharacterDetailsStateHolder.State.Data(
                            character = CharacterRAMDerails(
                                created = "2021-09-01",
                                episode = listOf("S01E01", "S02E05", "S03E10"),
                                gender = "Male",
                                id = 1,
                                image = "https://example.com/character1.png",
                                location = Location("Earth"),
                                name = "Rick Sanchez",
                                origin = Origin("Earth"),
                                species = "Human",
                                status = "Alive",
                                type = "Scientist",
                                url = "https://example.com/rick"
                            )
                        )
                    )
            }
        )
    }
}
