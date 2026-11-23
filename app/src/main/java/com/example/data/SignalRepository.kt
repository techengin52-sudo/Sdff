package com.example.data

import com.example.model.IndexSpec
import com.example.model.OptionSignal
import com.example.model.OptionType
import com.example.model.SignalDirection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round
import kotlin.random.Random

class SignalRepository(
    private val random: Random = Random.Default
) {
    private val bullishPatterns = listOf(
        "ORB Momentum Breakout",
        "VWAP Reclaim & Volume Surge",
        "Put Writing Support Bounce",
        "EMA 9/21 Bullish Crossover"
    )

    private val bearishPatterns = listOf(
        "Supply Zone Rejection",
        "Call OI Resistance Build",
        "VWAP Breakdown Retest",
        "Lower-High Impulse Breakdown"
    )

    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun generateBatch(savedSignalIds: Set<String> = emptySet()): List<OptionSignal> {
        val timestamp = timeFormatter.format(Date())
        return IndexSpec.entries.map { spec ->
            generateSignal(
                spec = spec,
                timestamp = timestamp,
                isSaved = false
            )
        }
    }

    fun generateSignal(
        spec: IndexSpec,
        timestamp: String = timeFormatter.format(Date()),
        isSaved: Boolean = false
    ): OptionSignal {
        val isBullish = random.nextBoolean()
        val optionType = if (isBullish) OptionType.CE else OptionType.PE
        val direction = if (isBullish) SignalDirection.STRONG_BUY else SignalDirection.STRONG_SELL

        val strikeOffset = random.nextInt(-2, 3)
        val strikePrice = spec.baseSpot + (strikeOffset * spec.strikeStep)
        val simulatedSpot = spec.baseSpot + random.nextInt(-spec.strikeStep, spec.strikeStep + 1)
        val entryPrice = random.nextInt(140, 320).toDouble()

        val target1 = roundOneDecimal(entryPrice * 1.15)
        val target2 = roundOneDecimal(entryPrice * 1.30)
        val stopLoss = roundOneDecimal(entryPrice * 0.85)
        val confidenceScore = random.nextInt(82, 96)

        val pattern = if (isBullish) {
            bullishPatterns[random.nextInt(bullishPatterns.size)]
        } else {
            bearishPatterns[random.nextInt(bearishPatterns.size)]
        }

        val id = "${spec.name}_${strikePrice}_${optionType.code}_${System.nanoTime()}"

        return OptionSignal(
            id = id,
            indexSpec = spec,
            indexName = spec.displayName,
            strikePrice = strikePrice,
            optionType = optionType,
            strike = "$strikePrice ${optionType.code}",
            direction = direction,
            signalType = direction.label,
            entry = entryPrice,
            target1 = target1,
            target2 = target2,
            stopLoss = stopLoss,
            confidenceScore = confidenceScore,
            confidence = "$confidenceScore%",
            setupPattern = pattern,
            simulatedSpot = simulatedSpot,
            generatedTime = timestamp,
            isSaved = isSaved
        )
    }

    private fun roundOneDecimal(value: Double): Double = round(value * 10.0) / 10.0
}
