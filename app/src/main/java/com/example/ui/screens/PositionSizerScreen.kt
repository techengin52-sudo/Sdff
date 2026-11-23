package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.IndexSpec
import com.example.model.PositionSizingState
import com.example.ui.components.SimulationModeBanner
import com.example.ui.theme.LocalTradingColors
import com.example.ui.theme.MonoPriceLarge
import com.example.ui.theme.MonoPriceMedium
import com.example.ui.theme.MonoPriceSmall
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PositionSizerScreen(
    sizing: PositionSizingState,
    onIndexSelected: (IndexSpec) -> Unit,
    onLotsChanged: (Int) -> Unit,
    onCapitalChanged: (Double) -> Unit,
    onEntryChanged: (Double) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current
    var entryText by remember(sizing.entryPrice) { mutableStateOf(sizing.entryPrice.toString()) }
    var capitalText by remember(sizing.capitalAmount) { mutableStateOf(sizing.capitalAmount.roundToInt().toString()) }

    LaunchedEffect(sizing.entryPrice) {
        if (entryText.toDoubleOrNull() != sizing.entryPrice) {
            entryText = sizing.entryPrice.toString()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 680.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimulationModeBanner()

            Text(
                text = "Position Size & Payoff Sizer",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Index Selector
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = tradingColors.elevatedCard),
                border = BorderStroke(1.dp, tradingColors.cardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contract & Lot Spec",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = sizing.strikeLabel,
                            style = MonoPriceMedium,
                            color = tradingColors.accentCyan
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IndexSpec.entries.forEach { spec ->
                            val selected = spec == sizing.selectedIndex
                            FilterChip(
                                selected = selected,
                                onClick = { onIndexSelected(spec) },
                                label = {
                                    Text(
                                        text = "${spec.displayName} (${spec.lotSize})",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("sizer_index_chip_${spec.name.lowercase()}")
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = entryText,
                            onValueChange = { raw ->
                                entryText = raw
                                raw.toDoubleOrNull()?.let { value ->
                                    if (value >= 5.0) onEntryChanged(value)
                                }
                            },
                            label = { Text("Entry Premium (₹)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = MonoPriceMedium,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sizer_entry_input")
                        )

                        OutlinedTextField(
                            value = capitalText,
                            onValueChange = { raw ->
                                capitalText = raw
                                raw.toDoubleOrNull()?.let { value ->
                                    if (value >= 5000.0) onCapitalChanged(value)
                                }
                            },
                            label = { Text("Trading Capital (₹)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = MonoPriceMedium,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sizer_capital_input")
                        )
                    }

                    // Lots Stepper & Slider
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Number of Lots",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${sizing.lotsCount} lots = ${sizing.totalUnits} units",
                                    style = MonoPriceSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onLotsChanged(sizing.lotsCount - 1) },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("sizer_lots_decrease")
                                ) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Decrease lots")
                                }
                                Text(
                                    text = "${sizing.lotsCount}",
                                    style = MonoPriceLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { onLotsChanged(sizing.lotsCount + 1) },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("sizer_lots_increase")
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Increase lots")
                                }
                            }
                        }

                        Slider(
                            value = sizing.lotsCount.toFloat(),
                            onValueChange = { onLotsChanged(it.roundToInt()) },
                            valueRange = 1f..20f,
                            steps = 18,
                            modifier = Modifier.testTag("sizer_lots_slider")
                        )
                    }
                }
            }

            // Projected Outcome Matrix
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = tradingColors.elevatedCard),
                border = BorderStroke(1.dp, tradingColors.cardBorder),
                modifier = Modifier.testTag("sizer_results_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Projected P&L & Risk Exposure",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutcomeTile(
                            title = "Capital Needed",
                            value = "₹${sizing.capitalRequired.roundToInt()}",
                            sub = "${sizing.totalUnits} qty @ ₹${sizing.entryPrice}",
                            accent = tradingColors.accentCyan,
                            modifier = Modifier.weight(1f)
                        )
                        OutcomeTile(
                            title = "Max SL Risk (-15%)",
                            value = "-₹${sizing.maxRiskAmount.roundToInt()}",
                            sub = "${String.format(Locale.US, "%.1f", sizing.capitalRiskPercent)}% of capital",
                            accent = tradingColors.bearish,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(color = tradingColors.cardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutcomeTile(
                            title = "Target 1 (+15%)",
                            value = "+₹${sizing.profitAtTarget1.roundToInt()}",
                            sub = "Exit @ ₹${sizing.target1Price}",
                            accent = tradingColors.bullish,
                            modifier = Modifier.weight(1f)
                        )
                        OutcomeTile(
                            title = "Target 2 (+30%)",
                            value = "+₹${sizing.profitAtTarget2.roundToInt()}",
                            sub = "Exit @ ₹${sizing.target2Price}",
                            accent = tradingColors.bullish,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OutcomeTile(
    title: String,
    value: String,
    sub: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MonoPriceLarge,
                color = accent
            )
            Text(
                text = sub,
                style = MonoPriceSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
