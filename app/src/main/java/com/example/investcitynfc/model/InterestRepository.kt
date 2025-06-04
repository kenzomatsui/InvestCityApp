package com.example.investcitynfc.model

class InterestRepository {
    private val simpleQuestions = listOf(
        InterestQuestion(1, InterestType.SIMPLE, "Você aplicou R$1.500 a 5% a.m. por 4 meses. Calcule os juros.", 300.0),
        InterestQuestion(2, InterestType.SIMPLE, "Você investiu R$2.000 a 2% a.m. por 10 meses. Calcule os juros.", 400.0),
        InterestQuestion(3, InterestType.SIMPLE, "Você depositou R$3.000 a 1,5% a.m. por 8 meses. Calcule os juros.", 360.0),
        InterestQuestion(4, InterestType.SIMPLE, "Você fez uma aplicação de R$5.000 a 4% a.m. por 12 meses. Calcule os juros.", 2400.0),
        InterestQuestion(5, InterestType.SIMPLE, "Você aplicou R$1.200 a 3,5% a.m. por 6 meses. Calcule os juros.", 252.0),
        InterestQuestion(6, InterestType.SIMPLE, "Você investiu R$4.500 a 6% a.m. por 3 meses. Calcule os juros.", 810.0),
        InterestQuestion(7, InterestType.SIMPLE, "Você aplicou R$800 a 2,5% a.m. por 9 meses. Calcule os juros.", 180.0),
        InterestQuestion(8, InterestType.SIMPLE, "Você fez uma aplicação de R$6.000 a 7% a.m. por 5 meses. Calcule os juros.", 2100.0),
        InterestQuestion(9, InterestType.SIMPLE, "Você aplicou R$2.000 a 4,5% a.m. por 7 meses. Calcule os juros.", 630.0),
        InterestQuestion(10, InterestType.SIMPLE, "Você investiu R$2.500 a 3% a.m. por 2 meses. Calcule os juros.", 150.0),
        InterestQuestion(11, InterestType.SIMPLE, "Você fez uma aplicação de R$10.000 a 0,8% a.m. por 15 meses. Calcule os juros.", 1200.0),
        InterestQuestion(12, InterestType.SIMPLE, "Você aplicou R$3.500 a 1,2% a.m. por 4 meses. Calcule os juros.", 168.0),
        InterestQuestion(13, InterestType.SIMPLE, "Você investiu R$7.000 a 3% a.m. por 6 meses. Calcule os juros.", 1260.0),
        InterestQuestion(14, InterestType.SIMPLE, "Você fez uma aplicação de R$1.000 a 5% a.m. por 10 meses. Calcule os juros.", 500.0),
        InterestQuestion(15, InterestType.SIMPLE, "Você aplicou R$4.000 a 1,8% a.m. por 8 meses. Calcule os juros.", 576.0),
        InterestQuestion(16, InterestType.SIMPLE, "Você investiu R$2.800 a 2% a.m. por 5 meses. Calcule os juros.", 280.0),
        InterestQuestion(17, InterestType.SIMPLE, "Você fez uma aplicação de R$6.500 a 4,3% a.m. por 11 meses. Calcule os juros.", 3128.5),
        InterestQuestion(18, InterestType.SIMPLE, "Você aplicou R$1.900 a 3,5% a.m. por 3 meses. Calcule os juros.", 199.5),
        InterestQuestion(19, InterestType.SIMPLE, "Você investiu R$2.700 a 6% a.m. por 9 meses. Calcule os juros.", 1458.0),
        InterestQuestion(20, InterestType.SIMPLE, "Você fez uma aplicação de R$8.000 a 2% a.m. por 4 meses. Calcule os juros.", 640.0)
    )

    private val compoundQuestions = listOf(
        InterestQuestion(1, InterestType.COMPOUND, "Você investiu R$1.500 a 5% ao mês por 2 meses. Calcule os juros.", 153.75),
        InterestQuestion(2, InterestType.COMPOUND, "Você aplicou R$2.000 a 3% ao mês por 4 meses. Calcule os juros.", 251.0),
        InterestQuestion(3, InterestType.COMPOUND, "Você investiu R$3.000 a 6% ao mês por 3 meses. Calcule os juros.", 573.05),
        InterestQuestion(4, InterestType.COMPOUND, "Você aplicou R$4.000 a 2,5% ao mês por 6 meses. Calcule os juros.", 637.10),
        InterestQuestion(5, InterestType.COMPOUND, "Você investiu R$2.500 a 4% ao mês por 5 meses. Calcule os juros.", 541.63),
        InterestQuestion(6, InterestType.COMPOUND, "Você aplicou R$3.200 a 7% ao mês por 2 meses. Calcule os juros.", 463.68),
        InterestQuestion(7, InterestType.COMPOUND, "Você investiu R$5.000 a 3,5% ao mês por 8 meses. Calcule os juros.", 1580.37),
        InterestQuestion(8, InterestType.COMPOUND, "Você aplicou R$1.800 a 5,5% ao mês por 3 meses. Calcule os juros.", 316.31),
        InterestQuestion(9, InterestType.COMPOUND, "Você investiu R$6.000 a 4% ao mês por 9 meses. Calcule os juros.", 2539.78),
        InterestQuestion(10, InterestType.COMPOUND, "Você aplicou R$2.200 a 2% ao mês por 12 meses. Calcule os juros.", 538.93),
        InterestQuestion(11, InterestType.COMPOUND, "Você investiu R$7.000 a 6% ao mês por 4 meses. Calcule os juros.", 1392.0),
        InterestQuestion(12, InterestType.COMPOUND, "Você aplicou R$3.500 a 8% ao mês por 5 meses. Calcule os juros.", 1535.0),
        InterestQuestion(13, InterestType.COMPOUND, "Você investiu R$4.800 a 1,5% ao mês por 6 meses. Calcule os juros.", 468.0),
        InterestQuestion(14, InterestType.COMPOUND, "Você aplicou R$2.000 a 10% ao mês por 3 meses. Calcule os juros.", 615.0),
        InterestQuestion(15, InterestType.COMPOUND, "Você investiu R$5.500 a 3,5% ao mês por 7 meses. Calcule os juros.", 1014.12),
        InterestQuestion(16, InterestType.COMPOUND, "Você aplicou R$1.200 a 2% ao mês por 10 meses. Calcule os juros.", 292.76),
        InterestQuestion(17, InterestType.COMPOUND, "Você investiu R$4.000 a 6% ao mês por 5 meses. Calcule os juros.", 3128.5),
        InterestQuestion(18, InterestType.COMPOUND, "Você aplicou R$2.800 a 4,5% ao mês por 4 meses. Calcule os juros.", 199.5),
        InterestQuestion(19, InterestType.COMPOUND, "Você investiu R$6.500 a 5% ao mês por 6 meses. Calcule os juros.", 1458.0),
        InterestQuestion(20, InterestType.COMPOUND, "Você aplicou R$3.000 a 7% ao mês por 2 meses. Calcule os juros.", 640.0)
    )

    fun getRandomSimpleQuestion(): InterestQuestion = simpleQuestions.random()
    fun getRandomCompoundQuestion(): InterestQuestion = compoundQuestions.random()
} 