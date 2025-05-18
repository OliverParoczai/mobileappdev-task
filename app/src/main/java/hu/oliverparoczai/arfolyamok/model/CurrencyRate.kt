package hu.oliverparoczai.arfolyamok.model

data class CurrencyRate(
    var id: String = "",
    var currencyCode: String = "",
    var rate: Double = 0.0,
    var timestamp: Long = 0
)