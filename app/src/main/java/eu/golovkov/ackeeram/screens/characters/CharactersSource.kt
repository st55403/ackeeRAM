package eu.golovkov.ackeeram.screens.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eu.golovkov.ackeeram.ApiService
import eu.golovkov.ackeeram.model.CharacterRAM
import eu.golovkov.ackeeram.repository.DataStorePreferenceRepository
import kotlinx.coroutines.flow.firstOrNull

class CharactersSource(
    private val apiService: ApiService,
    private val searchText: String?,
    dataStoreRepository: DataStorePreferenceRepository,
) : PagingSource<Int, CharacterRAM>() {

    private val favoriteIds = dataStoreRepository.getIds()

    override fun getRefreshKey(state: PagingState<Int, CharacterRAM>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterRAM> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getCharacters(
                page = page,
                name = searchText,
            )
            val favoriteIds = favoriteIds.firstOrNull() ?: emptySet()
            val characters = response.results.map { character ->
                character.copy(isFavorite = favoriteIds.contains(character.id))
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == response.info.pages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
