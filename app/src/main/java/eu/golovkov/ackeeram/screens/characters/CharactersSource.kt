package eu.golovkov.ackeeram.screens.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eu.golovkov.ackeeram.ApiService
import eu.golovkov.ackeeram.model.CharacterRAM

class CharactersSource(
    private val apiService: ApiService
) : PagingSource<Int, CharacterRAM>() {
    // TODO: consider to simplify this method
    override fun getRefreshKey(state: PagingState<Int, CharacterRAM>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterRAM> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getCharacters(page)
            val characters = response.results

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
