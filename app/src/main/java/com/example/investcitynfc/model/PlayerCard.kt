package com.example.investcitynfc.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PlayerCard(
    val playerId: String,
    var balance: Int
) {
    companion object {
        fun fromJson(json: String): PlayerCard? {
            return try {
                Json.decodeFromString<PlayerCard>(json)
            } catch (e: Exception) {
                null
            }
        }

        fun toJson(playerCard: PlayerCard): String {
            return Json.encodeToString(PlayerCard.serializer(), playerCard)
        }
    }
} 