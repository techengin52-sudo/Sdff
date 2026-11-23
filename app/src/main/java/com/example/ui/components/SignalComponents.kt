package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.OptionSignal
import com.example.model.SignalDirection
import com.example.ui.theme.LocalTradingColors
import com.example.ui.theme.MonoPriceLarge
import com.example.ui.theme.MonoPriceMedium
import com.example.ui.theme.MonoPriceSmall

@Composable
fun SimulationModeBanner(
    modifier: Modifier = Modifier,
    onInfoClick: (() -> Unit)? = null
) {
    val tradingColors = LocalTradingColors.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("simulation_mode_banner"),
        shape = RoundedCornerShape(14.dp),
        color = tradingColors.simulationContainer,
        border = BorderStroke(1.dp, tradingColors.simulation.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(tradingColors.simulation.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Science,
                    contentDescription = "Simulation mode indicator",
                    tint = tradingColors.simulation,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.simulation_banner_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = tradingColors.onSimulationContainer,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.simulation_banner_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (onInfoClick != null) {
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("simulation_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "View methodology and simulation notice",
                        tint = tradingColors.onSimulationContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MarketOverviewHero(
    totalSignals: Int,
    bullishCount: Int,
    bearishCount: Int,
    averageAccuracy: Int,
    lastUpdatedTime: String,
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("market_overview_hero"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, tradingColors.cardBorder),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_market_header_1795442690253),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.28f,
                modifier = Modifier
                    .matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.68f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INDEX OPTIONS DESK",
                            style = MaterialTheme.typography.labelMedium,
                            color = tradingColors.accentCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$totalSignals Active Setups",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, tradingColors.cardBorder)
                    ) {
                        Text(
                            text = if (lastUpdatedTime.isNotEmpty()) "Sync $lastUpdatedTime" else "Ready",
                            style = MonoPriceSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeroMetricPill(
                        label = "Bullish CE",
                        value = bullishCount.toString(),
                        accentColor = tradingColors.bullish,
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricPill(
                        label = "Bearish PE",
                        value = bearishCount.toString(),
                        accentColor = tradingColors.bearish,
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricPill(
                        label = "Avg Score",
                        value = "$averageAccuracy%",
                        accentColor = tradingColors.accentCyan,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroMetricPill(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.30f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MonoPriceLarge,
                color = accentColor
            )
        }
    }
}

@Composable
fun SignalCard(
    signal: OptionSignal,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onToggleSave: () -> Unit,
    onSizeTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current
    val isBullish = signal.direction == SignalDirection.STRONG_BUY
    val accentColor = if (isBullish) tradingColors.bullish else tradingColors.bearish
    val badgeBg = if (isBullish) tradingColors.bullishContainer else tradingColors.bearishContainer
    val badgeText = if (isBullish) tradingColors.onBullishContainer else tradingColors.onBearishContainer

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else tradingColors.cardBorder,
        animationSpec = spring(),
        label = "card_border"
    )
    val confidenceFraction by animateFloatAsState(
        targetValue = (signal.confidenceScore / 100f).coerceIn(0f, 1f),
        animationSpec = spring(),
        label = "confidence_bar"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onCardClick)
            .testTag("signal_card_${signal.indexSpec.name.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = tradingColors.elevatedCard),
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left directional accent spine
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row: Index, Strike, Bookmark
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = signal.indexSpec.exchange,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tradingColors.accentCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = signal.indexName,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = signal.strike,
                                style = MonoPriceLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Lot ${signal.indexSpec.lotSize}",
                                style = MonoPriceSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("bookmark_button_${signal.indexSpec.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = if (signal.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (signal.isSaved) {
                                stringResource(R.string.action_unsave)
                            } else {
                                stringResource(R.string.action_save)
                            },
                            tint = if (signal.isSaved) tradingColors.simulation else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Signal Direction Badge + Accuracy Score Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = badgeBg,
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isBullish) {
                                    Icons.AutoMirrored.Filled.TrendingUp
                                } else {
                                    Icons.AutoMirrored.Filled.TrendingDown
                                },
                                contentDescription = null,
                                tint = badgeText,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = signal.signalType,
                                style = MaterialTheme.typography.labelLarge,
                                color = badgeText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.label_accuracy),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = signal.confidence,
                                style = MonoPriceMedium,
                                color = accentColor
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(92.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(confidenceFraction)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(50))
                                    .background(accentColor)
                            )
                        }
                    }
                }

                // Setup Rationale + Spot Estimate
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = signal.setupPattern,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Spot ~${signal.simulatedSpot}",
                        style = MonoPriceSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 4-Cell Price Matrix (Entry, T1, T2, Stop Loss)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PriceLevelCell(
                        label = stringResource(R.string.label_entry),
                        price = "₹${signal.entry}",
                        subLabel = "Base",
                        accentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        modifier = Modifier.weight(1f)
                    )
                    PriceLevelCell(
                        label = stringResource(R.string.label_target_1),
                        price = "₹${signal.target1}",
                        subLabel = "+15%",
                        accentColor = tradingColors.bullish,
                        containerColor = tradingColors.bullishContainer.copy(alpha = 0.45f),
                        modifier = Modifier.weight(1f)
                    )
                    PriceLevelCell(
                        label = stringResource(R.string.label_target_2),
                        price = "₹${signal.target2}",
                        subLabel = "+30%",
                        accentColor = tradingColors.bullish,
                        containerColor = tradingColors.bullishContainer.copy(alpha = 0.65f),
                        modifier = Modifier.weight(1f)
                    )
                    PriceLevelCell(
                        label = stringResource(R.string.label_stop_loss),
                        price = "₹${signal.stopLoss}",
                        subLabel = "-15%",
                        accentColor = tradingColors.bearish,
                        containerColor = tradingColors.bearishContainer.copy(alpha = 0.55f),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Footer Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "R:R ${signal.riskRewardT2} • ${signal.generatedTime}",
                        style = MonoPriceSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedButton(
                        onClick = onSizeTrade,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, tradingColors.cardBorder),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("size_trade_button_${signal.indexSpec.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Size Lot",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceLevelCell(
    label: String,
    price: String,
    subLabel: String,
    accentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = price,
            style = MonoPriceMedium,
            color = accentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subLabel,
            style = MonoPriceSmall,
            color = accentColor.copy(alpha = 0.85f),
            maxLines = 1
        )
    }
}

@Composable
fun SignalDetailPane(
    signal: OptionSignal,
    onToggleSave: () -> Unit,
    onRegenerateIndex: () -> Unit,
    onOpenInSizer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current
    val isBullish = signal.direction == SignalDirection.STRONG_BUY
    val accentColor = if (isBullish) tradingColors.bullish else tradingColors.bearish

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .testTag("signal_detail_pane"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${signal.indexName} • ${signal.indexSpec.exchange}",
                    style = MaterialTheme.typography.labelMedium,
                    color = tradingColors.accentCyan
                )
                Text(
                    text = signal.strike,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${signal.optionType.fullName} • ${signal.setupPattern}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onToggleSave,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("detail_bookmark_button")
            ) {
                Icon(
                    imageVector = if (signal.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Toggle bookmark",
                    tint = if (signal.isSaved) tradingColors.simulation else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Visual Price Ladder
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, tradingColors.cardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "PAYOFF LADDER (1 LOT = ${signal.indexSpec.lotSize} QTY)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LadderRow(
                    title = "Target 2 (+30%)",
                    price = "₹${signal.target2}",
                    delta = "+${signal.reward2Points} pts  (R:R ${signal.riskRewardT2})",
                    color = tradingColors.bullish
                )
                LadderRow(
                    title = "Target 1 (+15%)",
                    price = "₹${signal.target1}",
                    delta = "+${signal.reward1Points} pts  (R:R ${signal.riskRewardT1})",
                    color = tradingColors.bullish
                )
                HorizontalDivider(color = tradingColors.cardBorder)
                LadderRow(
                    title = "Entry Zone",
                    price = "₹${signal.entry}",
                    delta = "${signal.signalType} (${signal.confidence})",
                    color = accentColor
                )
                HorizontalDivider(color = tradingColors.cardBorder)
                LadderRow(
                    title = "Stop Loss (-15%)",
                    price = "₹${signal.stopLoss}",
                    delta = "-${signal.riskPoints} pts max risk",
                    color = tradingColors.bearish
                )
            }
        }

        // 1-Lot Quick Impact Summary
        val lotRisk = kotlin.math.round(signal.riskPoints * signal.indexSpec.lotSize)
        val lotProfitT2 = kotlin.math.round(signal.reward2Points * signal.indexSpec.lotSize)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = tradingColors.bearishContainer.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "1-Lot Risk",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "-₹${lotRisk.toInt()}",
                        style = MonoPriceLarge,
                        color = tradingColors.bearish
                    )
                }
            }
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = tradingColors.bullishContainer.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "1-Lot Target 2",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "+₹${lotProfitT2.toInt()}",
                        style = MonoPriceLarge,
                        color = tradingColors.bullish
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRegenerateIndex,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
                    .testTag("detail_regenerate_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "New Setup")
            }

            Button(
                onClick = onOpenInSizer,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1.3f)
                    .minimumInteractiveComponentSize()
                    .testTag("detail_open_sizer_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Calculate,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Size Position")
            }
        }
    }
}

@Composable
private fun LadderRow(
    title: String,
    price: String,
    delta: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = delta,
                style = MonoPriceSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = price,
            style = MonoPriceLarge,
            color = color
        )
    }
}
