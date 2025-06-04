package com.example.investcitynfc.model

data class BankAccount(
    val id: Int,
    var ownerName: String = "",
    var balance: Double = 0.0
) 