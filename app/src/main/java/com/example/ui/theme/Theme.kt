package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TradingColors(
    val bullish: Color,
    val bullishContainer: Color,
    val onBullishContainer: Color,
    val bearish: Color,
    val bearishContainer: Color,
    val onBearishContainer: Color,
    val simulation: Color,
    val simulationContainer: Color,
    val onSimulationContainer: Color,
    val accentCyan: Color,
    val cardBorder: Color,
    val elevatedCard: Color
)

private val DarkTradingColors = TradingColors(
    bullish = BullishEmerald,
    bullishContainer = BullishEmeraldContainerDark.copy(alpha = 0.55f),
    onBullishContainer = Color(0xFF6EE7B7),
    bearish = BearishCrimson,
    bearishContainer = BearishCrimsonContainerDark.copy(alpha = 0.55f),
    onBearishContainer = Color(0xFFFCA5A5),
    simulation = SimulationAmber,
    simulationContainer = SimulationAmberContainerDark.copy(alpha = 0.75f),
    onSimulationContainer = Color(0xFFFCD34D),
    accentCyan = SignalCyan,
    cardBorder = ObsidianBorder,
    elevatedCard = ObsidianSurfaceElevated
)

private val LightTradingColors = TradingColors(
    bullish = Color(0xFF059669),
    bullishContainer = BullishEmeraldContainerLight,
    onBullishContainer = BullishOnEmeraldLight,
    bearish = Color(0xFFDC2626),
    bearishContainer = BearishCrimsonContainerLight,
    onBearishContainer = BearishOnCrimsonLight,
    simulation = Color(0xFFD97706),
    simulationContainer = SimulationAmberContainerLight,
    onSimulationContainer = SimulationOnAmberLight,
    accentCyan = SignalCyanDark,
    cardBorder = DaylightBorder,
    elevatedCard = DaylightSurface
)

val LocalTradingColors = staticCompositionLocalOf { DarkTradingColors }

private val DarkColorScheme = darkColorScheme(
    primary = BullishEmerald,
    onPrimary = Color(0xFF022C22),
    primaryContainer = BullishEmeraldContainerDark,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = SignalCyan,
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = SimulationAmber,
    onTertiary = Color(0xFF451A03),
    background = ObsidianBg,
    onBackground = SlateTextPrimary,
    surface = ObsidianSurface,
    onSurface = SlateTextPrimary,
    surfaceVariant = ObsidianSurfaceHigh,
    onSurfaceVariant = SlateTextSecondary,
    surfaceContainerLowest = ObsidianBg,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurfaceElevated,
    surfaceContainerHigh = ObsidianSurfaceHigh,
    outline = ObsidianBorder,
    error = BearishCrimson,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = BullishEmeraldContainerLight,
    onPrimaryContainer = BullishOnEmeraldLight,
    secondary = SignalCyanDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF075985),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    background = DaylightBg,
    onBackground = InkTextPrimary,
    surface = DaylightSurface,
    onSurface = InkTextPrimary,
    surfaceVariant = DaylightSurfaceHigh,
    onSurfaceVariant = InkTextSecondary,
    surfaceContainerLowest = DaylightSurface,
    surfaceContainerLow = DaylightSurfaceElevated,
    surfaceContainer = DaylightSurface,
    surfaceContainerHigh = DaylightSurfaceHigh,
    outline = DaylightBorder,
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun IndexPulseTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val tradingColors = if (darkTheme) DarkTradingColors else LightTradingColors

    CompositionLocalProvider(LocalTradingColors provides tradingColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
