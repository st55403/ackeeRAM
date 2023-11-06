package eu.golovkov.ackeeram.screens.characters

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
import com.ramcosta.composedestinations.annotation.RootNavGraph
import eu.golovkov.ackeeram.R

@RootNavGraph(start = true)
@Destination()
@Composable
fun CharactersScreen() {
    Characters()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Characters() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.characters_title)
                    )
                }
            )
        }
    ) {
        Text(
            text = stringResource(R.string.characters_title),
            modifier = Modifier.padding(it)
        )
    }
}

// TODO: create custom preview annotation with light and dark theme
@Preview
@Composable
private fun CharactersPreview() {

}