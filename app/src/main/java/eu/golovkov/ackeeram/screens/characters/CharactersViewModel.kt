package eu.golovkov.ackeeram.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.golovkov.ackeeram.StatefulLayoutState
import eu.golovkov.ackeeram.app
import eu.golovkov.ackeeram.model.CharacterRAM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel(), CharactersStateHolder {
    private val mutableState: MutableStateFlow<CharactersStateHolder.State> =
        MutableStateFlow(CharactersStateHolder.State.Data())
    override val state: StateFlow<CharactersStateHolder.State> = mutableState.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            mutableState.value = try {
                val result = Pager(
                    config = PagingConfig(pageSize = 15, initialLoadSize = 15),
                    pagingSourceFactory = { CharactersSource(app.apiService) },
                ).flow.cachedIn(viewModelScope)
                CharactersStateHolder.State.Data(
                    characters = result
                )
            } catch (e: Exception) {
                CharactersStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface CharactersStateHolder : CharactersStateTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val characters: Flow<PagingData<CharacterRAM>>? = null,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            object Empty : Message // TODO: use this to show when searching name etc 
            class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}

interface CharactersStateTransformer {
    fun onLoadStatesChanged(loadStates: CombinedLoadStates, itemCount: Int) {}
}
