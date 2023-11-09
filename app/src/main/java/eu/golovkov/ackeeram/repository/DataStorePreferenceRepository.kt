package eu.golovkov.ackeeram.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import eu.golovkov.ackeeram.app
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferenceRepository {
    private val dataStore: DataStore<Preferences> = app.dataStore

    private val favoriteCharacters = stringSetPreferencesKey("favorite_character_ids")

    fun getIds(): Flow<Set<Int>> = dataStore.data.map { preferences ->
        preferences[favoriteCharacters]?.map { it.toInt() }?.toSet() ?: emptySet()
    }

    suspend fun updateIds(id: Int) {
        dataStore.edit { preferences ->
            val currentIds = preferences[favoriteCharacters] ?: emptySet()
            val updatedIds = currentIds.let { ids ->
                if (ids.contains(id.toString())) ids - id.toString() else ids + id.toString()
            }
            preferences[favoriteCharacters] = updatedIds
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
