package eu.golovkov.ackeeram.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterRAMDerails(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String? = null,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

@Serializable
data class Location(
    val name: String,
)

@Serializable
data class Origin(
    val name: String,
)
