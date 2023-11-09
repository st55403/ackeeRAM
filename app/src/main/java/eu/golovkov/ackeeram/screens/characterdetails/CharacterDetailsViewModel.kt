package eu.golovkov.ackeeram.screens.characterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.golovkov.ackeeram.StatefulLayoutState
import eu.golovkov.ackeeram.app
import eu.golovkov.ackeeram.asData
import eu.golovkov.ackeeram.model.CharacterRAMDerails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val characterId: Int,
) : ViewModel(), CharacterDetailsStateHolder {
    private val mutableState: MutableStateFlow<CharacterDetailsStateHolder.State> =
        MutableStateFlow(CharacterDetailsStateHolder.State.Data())
    override val state: StateFlow<CharacterDetailsStateHolder.State> = mutableState.asStateFlow()

    init {
        loadCharacterDetails(characterId)
    }

    private fun loadCharacterDetails(characterId: Int) {
        mutableState.value = CharacterDetailsStateHolder.State.Loading
        viewModelScope.launch {
            mutableState.value = try {
                val isFavorite = app.dataStoreRepository.getIds().first().contains(characterId)
                val result = app.apiService.getCharacterDetails(characterId)
                CharacterDetailsStateHolder.State.Data(
                    character = result,
                    isFavorite = isFavorite
                )
            } catch (e: Exception) {
                CharacterDetailsStateHolder.State.Message.Error(e.message)
            }
        }
    }

    override fun changeFavorite() {
        val dataState = state.value.asData() ?: return
        viewModelScope.launch {
            try {
                app.dataStoreRepository.updateIds(characterId)
                mutableState.value = dataState.copy(
                    isFavorite = !dataState.isFavorite,
                    wasChanged = true,
                )
            } catch (e: Exception) {
                mutableState.value = CharacterDetailsStateHolder.State.Message.Error(e.message)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val characterId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CharacterDetailsViewModel(characterId) as T
    }
}

interface CharacterDetailsStateHolder : CharacterDetailsTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val character: CharacterRAMDerails? = null,
            val isFavorite: Boolean = false,
            val wasChanged: Boolean = false,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}

interface CharacterDetailsTransformer {
    fun changeFavorite() {}
}
