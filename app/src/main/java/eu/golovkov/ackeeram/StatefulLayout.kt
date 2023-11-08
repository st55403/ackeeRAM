package eu.golovkov.ackeeram

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Suppress("UNCHECKED_CAST")
@Composable
fun <Data, Message, Loading> StatefulLayout(
    state: StatefulLayoutState<Data, Message, Loading>,
    modifier: Modifier = Modifier,
    message: @Composable BoxScope.(Message) -> Unit = { messageNotImplementedError() },
    loading: @Composable BoxScope.(Loading) -> Unit = { LoadingContent() },
    content: @Composable BoxScope.(Data) -> Unit,
) where
        Data : StatefulLayoutState.Data,
        Message : StatefulLayoutState.Message,
        Loading : StatefulLayoutState.Loading {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box {
            when (state) {
                is StatefulLayoutState.Data -> content(state as Data)
                is StatefulLayoutState.Message -> message(state as Message)
                is StatefulLayoutState.Loading -> loading(state as Loading)
                else -> error("Aah")
            }
        }
    }
}

private fun messageNotImplementedError(): Nothing =
    throw NotImplementedError("Message content is not implemented, define `message = MessageLayout(...)` manually")

// TODO pass modifier instead of extending BoxScope
@Composable
private fun BoxScope.LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
    )
}

interface StatefulLayoutState<
        Data : StatefulLayoutState.Data,
        Message : StatefulLayoutState.Message,
        Loading : StatefulLayoutState.Loading,
        > {
    interface Data
    interface Message
    interface Loading
}

/** Safely casts the state to its data state. Returns `null` when it's some other state. */
@Suppress("UNCHECKED_CAST")
fun <Data, State> State.asData(): Data?
        where Data : StatefulLayoutState.Data,
              State : StatefulLayoutState<Data, out StatefulLayoutState.Message, out StatefulLayoutState.Loading> =
    this as? Data
