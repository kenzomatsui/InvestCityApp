package com.example.investcitynfc.model

data class Card(
    val id: Int,
    val type: CardType,
    val title: String,
    val description: String,
    val emoji: String,
    val effect: CardEffect
) 