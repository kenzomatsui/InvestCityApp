package com.example.investcitynfc.model

class CardRepository {
    private val fortuneCards = listOf(
        Card(
            id = 1,
            type = CardType.FORTUNE,
            title = "Dividendos Esquecidos",
            description = "Você recebeu dividendos de uma ação esquecida!",
            emoji = "💰",
            effect = CardEffect.AddMoney(300)
        ),
        Card(
            id = 2,
            type = CardType.FORTUNE,
            title = "Valorização do Imóvel",
            description = "Seu imóvel valorizou com a chegada de um shopping na região.",
            emoji = "📈",
            effect = CardEffect.AddRentMonth("") // Property ID will be set when used
        ),
        Card(
            id = 3,
            type = CardType.FORTUNE,
            title = "Aplicação Especial",
            description = "O banco te ofereceu uma aplicação especial.",
            emoji = "🏦",
            effect = CardEffect.DoubleRent("") // Property ID will be set when used
        ),
        Card(
            id = 4,
            type = CardType.FORTUNE,
            title = "Reembolso do IR",
            description = "Reembolso do imposto de renda caiu na conta.",
            emoji = "💳",
            effect = CardEffect.AddMoney(400)
        ),
        Card(
            id = 5,
            type = CardType.FORTUNE,
            title = "Curso de Finanças",
            description = "Você fez um curso rápido sobre finanças.",
            emoji = "🧠",
            effect = CardEffect.SkipNextMisfortune()
        ),
        Card(
            id = 6,
            type = CardType.FORTUNE,
            title = "Parceria Bem-sucedida",
            description = "Parceria bem-sucedida com outro jogador.",
            emoji = "🤝",
            effect = CardEffect.AddMoney(200)
        ),
        Card(
            id = 7,
            type = CardType.FORTUNE,
            title = "Melhorias na Propriedade",
            description = "Você fez melhorias em uma propriedade.",
            emoji = "🛠️",
            effect = CardEffect.IncreaseRentRate("", 0.002) // Property ID will be set when used
        ),
        Card(
            id = 8,
            type = CardType.FORTUNE,
            title = "Destaque na Revista",
            description = "Foi destaque na revista \"Investidores do Ano\"!",
            emoji = "🌟",
            effect = CardEffect.AddRentMonth("") // Property ID will be set when used
        ),
        Card(
            id = 9,
            type = CardType.FORTUNE,
            title = "Prêmio de Inovação",
            description = "Prêmio de inovação!",
            emoji = "🏆",
            effect = CardEffect.AddMoney(500)
        ),
        Card(
            id = 10,
            type = CardType.FORTUNE,
            title = "Dívida Recuperada",
            description = "Recuperou uma dívida antiga.",
            emoji = "🧾",
            effect = CardEffect.ReceiveFromPlayer(250)
        )
    )

    private val misfortuneCards = listOf(
        Card(
            id = 11,
            type = CardType.MISFORTUNE,
            title = "Reforma Emergencial",
            description = "Uma reforma emergencial foi necessária.",
            emoji = "🔧",
            effect = CardEffect.PayMoney(300)
        ),
        Card(
            id = 12,
            type = CardType.MISFORTUNE,
            title = "Crise Financeira",
            description = "Crise no mercado financeiro.",
            emoji = "📉",
            effect = CardEffect.HalveRent("") // Property ID will be set when used
        ),
        Card(
            id = 13,
            type = CardType.MISFORTUNE,
            title = "Vandalismo",
            description = "Sua propriedade sofreu vandalismo.",
            emoji = "🏚️",
            effect = CardEffect.LoseRentMonth("") // Property ID will be set when used
        ),
        Card(
            id = 14,
            type = CardType.MISFORTUNE,
            title = "Cobrança Indevida",
            description = "Cobrança indevida no cartão.",
            emoji = "💳",
            effect = CardEffect.PayMoney(200)
        ),
        Card(
            id = 15,
            type = CardType.MISFORTUNE,
            title = "Conta de Energia Alta",
            description = "A conta de energia da sua empresa veio altíssima.",
            emoji = "🔌",
            effect = CardEffect.PayMoney(150)
        ),
        Card(
            id = 16,
            type = CardType.MISFORTUNE,
            title = "Problema de Saúde",
            description = "Problema de saúde inesperado.",
            emoji = "🦠",
            effect = CardEffect.PayMoney(400)
        ),
        Card(
            id = 17,
            type = CardType.MISFORTUNE,
            title = "Erro no Imposto",
            description = "Erro na declaração de imposto.",
            emoji = "🚫",
            effect = CardEffect.PayMoney(250)
        ),
        Card(
            id = 18,
            type = CardType.MISFORTUNE,
            title = "Incêndio",
            description = "Incêndio atinge um de seus imóveis.",
            emoji = "🧯",
            effect = CardEffect.ZeroRent("") // Property ID will be set when used
        ),
        Card(
            id = 19,
            type = CardType.MISFORTUNE,
            title = "Processo Judicial",
            description = "Processo judicial travou sua propriedade.",
            emoji = "⚖️",
            effect = CardEffect.BlockRent("", 2) // Property ID will be set when used
        ),
        Card(
            id = 20,
            type = CardType.MISFORTUNE,
            title = "Golpe em Criptomoeda",
            description = "Investimento em criptomoeda foi golpe.",
            emoji = "🤷",
            effect = CardEffect.PayMoney(350)
        )
    )

    fun getRandomFortuneCard(): Card = fortuneCards.random()
    fun getRandomMisfortuneCard(): Card = misfortuneCards.random()
    fun getAllCards(): List<Card> = fortuneCards + misfortuneCards
} 