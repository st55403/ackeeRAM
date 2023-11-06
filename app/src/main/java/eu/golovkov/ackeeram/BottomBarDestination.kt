package eu.golovkov.ackeeram

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.Direction
import eu.golovkov.ackeeram.screens.destinations.CharactersScreenDestination
import eu.golovkov.ackeeram.screens.destinations.Destination
import eu.golovkov.ackeeram.screens.destinations.FavoritesScreenDestination

enum class BottomBarDestination(
    val direction: Direction,
    @DrawableRes val icon: Int,
    @StringRes val titleRes: Int,
) {
    CHARACTERS(
        direction = CharactersScreenDestination,
        icon = R.drawable.ic_characters,
        titleRes = R.string.bottom_bar_characters_title,
    ),
    FAVORITES(
        direction = FavoritesScreenDestination,
        icon = R.drawable.ic_favorites,
        titleRes = R.string.bottom_bar_favorites_title,
    );
    companion object {
        private val destinations = listOf(
            CharactersScreenDestination,
            FavoritesScreenDestination
        )

        fun containDestination(destination: Destination?): Boolean {
            return destinations.contains(destination)
        }
    }
}
