package com.example.model

enum class SignalDirection(val label: String) {
    STRONG_BUY("STRONG BUY"),
    STRONG_SELL("STRONG SELL")
}

enum class OptionType(val code: String, val fullName: String) {
    CE("CE", "Call Option"),
    PE("PE", "Put Option")
}

enum class IndexSpec(
    val displayName: String,
    val exchange: String,
    val baseSpot: Int,
    val strikeStep: Int,
    val lotSize: Int
) {
    NIFTY_50(
        displayName = "NIFTY 50",
        exchange = "NSE",
        baseSpot = 24200,
        strikeStep = 50,
        lotSize = 75
    ),
    BANK_NIFTY(
        displayName = "BANK NIFTY",
        exchange = "NSE",
        baseSpot = 51800,
        strikeStep = 100,
        lotSize = 30
    ),
    SENSEX(
        displayName = "SENSEX",
        exchange = "BSE",
        baseSpot = 79600,
        strikeStep = 100,
        lotSize = 20
    ),
    FINNIFTY(
        displayName = "FINNIFTY",
        exchange = "NSE",
        baseSpot = 23900,
        strikeStep = 50,
        lotSize = 65
    )
}

data class OptionSignal(
    val id: String,
    val indexSpec: IndexSpec,
    val indexName: String,
    val strikePrice: Int,
    val optionType: OptionType,
    val strike: String,
    val direction: SignalDirection,
    val signalType: String,
    val entry: Double,
    val target1: Double,
    val target2: Double,
    val stopLoss: Double,
    val confidenceScore: Int,
    val confidence: String,
    val setupPattern: String,
    val simulatedSpot: Int,
    val generatedTime: String,
    val isSaved: Boolean = false
) {
    val riskPoints: Double get() = ((entry - stopLoss) * 10.0).let { kotlin.math.round(it) / 10.0 }
    val reward1Points: Double get() = ((target1 - entry) * 10.0).let { kotlin.math.round(it) / 10.0 }
    val reward2Points: Double get() = ((target2 - entry) * 10.0).let { kotlin.math.round(it) / 10.0 }
    val riskRewardT1: String get() = if (riskPoints > 0) "1 : ${formatOneDecimal(reward1Points / riskPoints)}" else "1 : 1.0"
    val riskRewardT2: String get() = if (riskPoints > 0) "1 : ${formatOneDecimal(reward2Points / riskPoints)}" else "1 : 2.0"

    private fun formatOneDecimal(value: Double): String {
        val rounded = kotlin.math.round(value * 10.0) / 10.0
        return rounded.toString()
    }
}

enum class SignalFilter(val label: String) {
    ALL("All"),
    STRONG_BUY("Strong Buy"),
    STRONG_SELL("Strong Sell"),
    SAVED("Saved")
}

enum class AppTab(val label: String) {
    SIGNALS("Signals"),
    SIZER("Sizer"),
    SAVED("Saved")
}

data class PositionSizingState(
    val selectedIndex: IndexSpec = IndexSpec.NIFTY_50,
    val strikeLabel: String = "24200 CE",
    val entryPrice: Double = 210.0,
    val target1Price: Double = 241.5,
    val target2Price: Double = 273.0,
    val stopLossPrice: Double = 178.5,
    val capitalAmount: Double = 50000.0,
    val lotsCount: Int = 1
) {
    val totalUnits: Int get() = lotsCount * selectedIndex.lotSize
    val capitalRequired: Double get() = entryPrice * totalUnits
    val maxRiskAmount: Double get() = ((entryPrice - stopLossPrice).coerceAtLeast(0.0)) * totalUnits
    val profitAtTarget1: Double get() = ((target1Price - entryPrice).coerceAtLeast(0.0)) * totalUnits
    val profitAtTarget2: Double get() = ((target2Price - entryPrice).coerceAtLeast(0.0)) * totalUnits
    val capitalRiskPercent: Double
        get() = if (capitalAmount > 0) (maxRiskAmount / capitalAmount) * 100.0 else 0.0
}
