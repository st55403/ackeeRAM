package eu.golovkov.ackeeram

import eu.golovkov.ackeeram.model.CharacterRAMDerails
import eu.golovkov.ackeeram.model.CharacterResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface ApiService {
    suspend fun getCharacters(page: Int, name: String? = null): CharacterResponse
    suspend fun getCharacterDetails(id: Int): CharacterRAMDerails

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }

        fun create(baseUrl: String): ApiService {
            return ApiServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                        logger = object : Logger {
                            override fun log(message: String) =
                                println(message)
                        }
                    }
                    install(ContentNegotiation) {
                        json(json)
                        engine {
                            connectTimeout = 60_000
                            socketTimeout = 60_000
                        }
                    }
                    defaultRequest {
                        url(baseUrl)
                        headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
                        headers.append(HttpHeaders.Accept, ContentType.Any)
                    }
                }
            )
        }
    }
}
