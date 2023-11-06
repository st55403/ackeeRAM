package eu.golovkov.ackeeram.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination

@Destination()
@Composable
fun FavoritesScreen() {
    Favorites()
}

@Composable
private fun Favorites() {
    Text(text = "Favorites")
}

@Preview
@Composable
private fun FavoritesPreview() {

}
