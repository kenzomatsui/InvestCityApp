package com.example.investcitynfc.model

sealed class CardEffect {
    data class AddMoney(val amount: Int) : CardEffect()
    data class PayMoney(val amount: Int) : CardEffect()
    data class AddRentMonth(val propertyId: String) : CardEffect()
    data class DoubleRent(val propertyId: String) : CardEffect()
    data class IncreaseRentRate(val propertyId: String, val rate: Double) : CardEffect()
    class SkipNextMisfortune : CardEffect()
    data class ReceiveFromPlayer(val amount: Int) : CardEffect()
    data class HalveRent(val propertyId: String) : CardEffect()
    data class LoseRentMonth(val propertyId: String) : CardEffect()
    data class ZeroRent(val propertyId: String) : CardEffect()
    data class BlockRent(val propertyId: String, val rounds: Int) : CardEffect()
} 