package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.IndexSpec
import com.example.model.OptionSignal
import com.example.model.SignalFilter
import com.example.ui.components.MarketOverviewHero
import com.example.ui.components.SignalCard
import com.example.ui.components.SignalDetailPane
import com.example.ui.components.SimulationModeBanner
import com.example.ui.theme.LocalTradingColors
import com.example.ui.viewmodel.SignalsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalsScreen(
    uiState: SignalsUiState,
    onRefreshAll: () -> Unit,
    onRegenerateIndex: (IndexSpec) -> Unit,
    onSelectFilter: (SignalFilter) -> Unit,
    onInspectSignal: (OptionSignal?) -> Unit,
    onToggleSave: (OptionSignal) -> Unit,
    onSizeTrade: (OptionSignal) -> Unit,
    onOpenMethodologyDialog: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        val isExpandedLayout = maxWidth >= 840.dp

        if (!isExpandedLayout && uiState.inspectedSignal != null) {
            BackHandler {
                onInspectSignal(null)
            }
        }

        if (isExpandedLayout) {
            // Canonical List-Detail Layout for Tablets / Foldables
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SignalsFeedList(
                    uiState = uiState,
                    onRefreshAll = onRefreshAll,
                    onSelectFilter = onSelectFilter,
                    onInspectSignal = onInspectSignal,
                    onToggleSave = onToggleSave,
                    onSizeTrade = onSizeTrade,
                    onOpenMethodologyDialog = onOpenMethodologyDialog,
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight()
                )

                val selectedSignal = uiState.inspectedSignal ?: uiState.filteredSignals.firstOrNull()
                Card(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxHeight()
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = tradingColors.elevatedCard),
                    border = BorderStroke(1.dp, tradingColors.cardBorder)
                ) {
                    if (selectedSignal != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            SignalDetailPane(
                                signal = selectedSignal,
                                onToggleSave = { onToggleSave(selectedSignal) },
                                onRegenerateIndex = { onRegenerateIndex(selectedSignal.indexSpec) },
                                onOpenInSizer = { onSizeTrade(selectedSignal) }
                            )
                        }
                    } else {
                        EmptyDetailPlaceholder()
                    }
                }
            }
        } else {
            // Compact Mobile Layout
            SignalsFeedList(
                uiState = uiState,
                onRefreshAll = onRefreshAll,
                onSelectFilter = onSelectFilter,
                onInspectSignal = onInspectSignal,
                onToggleSave = onToggleSave,
                onSizeTrade = onSizeTrade,
                onOpenMethodologyDialog = onOpenMethodologyDialog,
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .align(Alignment.TopCenter)
            )

            val inspected = uiState.inspectedSignal
            if (inspected != null) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ModalBottomSheet(
                    onDismissRequest = { onInspectSignal(null) },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("signal_detail_bottom_sheet")
                ) {
                    SignalDetailPane(
                        signal = inspected,
                        onToggleSave = { onToggleSave(inspected) },
                        onRegenerateIndex = { onRegenerateIndex(inspected.indexSpec) },
                        onOpenInSizer = { onSizeTrade(inspected) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SignalsFeedList(
    uiState: SignalsUiState,
    onRefreshAll: () -> Unit,
    onSelectFilter: (SignalFilter) -> Unit,
    onInspectSignal: (OptionSignal?) -> Unit,
    onToggleSave: (OptionSignal) -> Unit,
    onSizeTrade: (OptionSignal) -> Unit,
    onOpenMethodologyDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.testTag("signals_feed_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "simulation_banner") {
            SimulationModeBanner(onInfoClick = onOpenMethodologyDialog)
        }

        item(key = "hero_summary") {
            MarketOverviewHero(
                totalSignals = uiState.signals.size,
                bullishCount = uiState.bullishCount,
                bearishCount = uiState.bearishCount,
                averageAccuracy = uiState.averageConfidence,
                lastUpdatedTime = uiState.lastUpdatedTime
            )
        }

        item(key = "filter_chips") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SignalFilter.entries.forEach { filter ->
                    val count = when (filter) {
                        SignalFilter.ALL -> uiState.signals.size
                        SignalFilter.STRONG_BUY -> uiState.bullishCount
                        SignalFilter.STRONG_SELL -> uiState.bearishCount
                        SignalFilter.SAVED -> uiState.savedSignals.size
                    }
                    FilterChip(
                        selected = uiState.selectedFilter == filter,
                        onClick = { onSelectFilter(filter) },
                        label = {
                            Text(
                                text = "${filter.label} ($count)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("filter_chip_${filter.name.lowercase()}")
                    )
                }
            }
        }

        if (uiState.filteredSignals.isEmpty()) {
            item(key = "empty_state") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    EmptySignalsState(
                        isSavedFilter = uiState.selectedFilter == SignalFilter.SAVED,
                        onResetOrRefresh = {
                            if (uiState.selectedFilter == SignalFilter.SAVED) {
                                onSelectFilter(SignalFilter.ALL)
                            } else {
                                onRefreshAll()
                            }
                        }
                    )
                }
            }
        } else {
            items(
                items = uiState.filteredSignals,
                key = { it.id }
            ) { signal ->
                SignalCard(
                    signal = signal,
                    isSelected = uiState.inspectedSignal?.id == signal.id,
                    onCardClick = { onInspectSignal(signal) },
                    onToggleSave = { onToggleSave(signal) },
                    onSizeTrade = { onSizeTrade(signal) }
                )
            }
        }
    }
}

@Composable
private fun EmptySignalsState(
    isSavedFilter: Boolean,
    onResetOrRefresh: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = if (isSavedFilter) {
                    stringResource(R.string.empty_saved_title)
                } else {
                    stringResource(R.string.empty_filtered_title)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (isSavedFilter) {
                    stringResource(R.string.empty_saved_desc)
                } else {
                    stringResource(R.string.empty_filtered_desc)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onResetOrRefresh,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("empty_state_action_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = if (isSavedFilter) "Show All Signals" else "Refresh Setups")
            }
        }
    }
}

@Composable
private fun EmptyDetailPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Insights,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Select a signal setup",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Tap any index option card to inspect its target ladder and lot risk.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
