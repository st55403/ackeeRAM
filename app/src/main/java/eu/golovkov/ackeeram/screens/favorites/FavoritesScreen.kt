package eu.golovkov.ackeeram.screens.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import eu.golovkov.ackeeram.R

@Destination()
@Composable
fun FavoritesScreen() {
    Favorites()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Favorites() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorites_title)
                    )
                }
            )
        }
    ) {
        Text(
            text = stringResource(R.string.favorites_title),
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
private fun FavoritesPreview() {

}
