package eu.golovkov.ackeeram

import eu.golovkov.ackeeram.model.CharacterRAMDerails
import eu.golovkov.ackeeram.model.CharacterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {
    override suspend fun getCharacters(page: Int): CharacterResponse =
        client.get("character") {
            parameter("page", page)
        }.body()

    override suspend fun getCharacterDetails(id: Int): CharacterRAMDerails =
        client.get("character/$id").body()
}
