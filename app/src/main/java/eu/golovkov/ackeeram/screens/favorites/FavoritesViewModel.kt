package eu.golovkov.ackeeram.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.golovkov.ackeeram.StatefulLayoutState
import eu.golovkov.ackeeram.app
import eu.golovkov.ackeeram.model.CharacterRAM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel(), FavoritesStateHolder {
    private val mutableState: MutableStateFlow<FavoritesStateHolder.State> =
        MutableStateFlow(FavoritesStateHolder.State.Loading)
    override val state: StateFlow<FavoritesStateHolder.State> = mutableState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        mutableState.value = FavoritesStateHolder.State.Loading
        viewModelScope.launch {
            mutableState.value = try {
                val savedIds = app.dataStoreRepository.getIds().first().toList()
                if (savedIds.isEmpty()) {
                    FavoritesStateHolder.State.Message.Empty
                } else {
                    val result = app.apiService.getFavorites(savedIds).map {
                        it.copy(isFavorite = true)
                    }
                    FavoritesStateHolder.State.Data(
                        characters = result,
                    )
                }
            } catch (e: Exception) {
                FavoritesStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface FavoritesStateHolder {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val characters: List<CharacterRAM>,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            object Empty : Message
            data class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}
