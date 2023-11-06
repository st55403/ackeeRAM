package eu.golovkov.ackeeram.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination()
@Composable
fun CharactersScreen() {
    Characters()
}

@Composable
private fun Characters() {
    Text(text = "Characters")
}

// TODO: create custom preview annotation with light and dark theme
@Preview
@Composable
private fun CharactersPreview() {

}