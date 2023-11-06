package eu.golovkov.ackeeram

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {
    override suspend fun getCharacters(): List<Character> =
        client.get("character").body()

    override suspend fun getCharacter(id: Int): Character =
        client.get("character/$id").body()
}
