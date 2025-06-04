package com.example.investcitynfc.model

data class InterestQuestion(
    val id: Int,
    val type: InterestType,
    val question: String,
    val answer: Double
)

enum class InterestType {
    SIMPLE,
    COMPOUND
} 