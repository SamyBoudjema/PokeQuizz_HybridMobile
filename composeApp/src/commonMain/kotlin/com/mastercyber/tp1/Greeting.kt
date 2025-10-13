package com.mastercyber.tp1

import com.mastercyber.tp1.models.Pokemon
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}!"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchPokemon(): String? {
        val random = (1..1025).random()
        val response: Pokemon = client.get("https://tyradex.vercel.app/api/v1/pokemon/$random").body()
        return response.name?.fr ?: "Unknown"
    }

    suspend fun fetchPokemonWithImage(): Triple<Pokemon?, ByteArray?, ByteArray?> {
        val random = (1..1025).random()
        val pokemon: Pokemon = client.get("https://tyradex.vercel.app/api/v1/pokemon/$random").body()

        val imageBytes = pokemon.sprites?.regular?.let { url ->
            try {
                client.get(url).body<ByteArray>()
            } catch (e: Exception) {
                null
            }
        }

        return Triple(pokemon, imageBytes, imageBytes)
    }
}
