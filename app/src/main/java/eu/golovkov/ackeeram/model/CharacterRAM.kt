package eu.golovkov.ackeeram.model

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@Serializable
data class CharacterRAM(
    val id: Int,
    val name: String,
    val status: String,
    val image: String,
)

@Serializable
data class CharacterResponse(
    val info: Info,
    val results: List<CharacterRAM>
)
