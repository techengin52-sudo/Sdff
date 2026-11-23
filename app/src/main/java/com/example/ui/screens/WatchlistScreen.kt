package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.OptionSignal
import com.example.ui.components.SignalCard
import com.example.ui.theme.LocalTradingColors

@Composable
fun WatchlistScreen(
    savedSignals: List<OptionSignal>,
    onInspectSignal: (OptionSignal) -> Unit,
    onRemoveSaved: (String) -> Unit,
    onClearAll: () -> Unit,
    onSizeTrade: (OptionSignal) -> Unit,
    onBrowseSignals: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val tradingColors = LocalTradingColors.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .testTag("watchlist_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "watchlist_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Saved Setups (${savedSignals.size})",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Session bookmarks for quick R:R review",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (savedSignals.isNotEmpty()) {
                    TextButton(
                        onClick = onClearAll,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("clear_saved_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteSweep,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text("Clear")
                    }
                }
            }
        }

        if (savedSignals.isEmpty()) {
            item(key = "watchlist_empty") {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, tradingColors.cardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BookmarkBorder,
                            contentDescription = null,
                            tint = tradingColors.simulation,
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            text = stringResource(R.string.empty_saved_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.empty_saved_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onBrowseSignals,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("watchlist_browse_button")
                        ) {
                            Text("Browse Signal Feed")
                        }
                    }
                }
            }
        } else {
            items(
                items = savedSignals,
                key = { "saved_${it.id}" }
            ) { signal ->
                SignalCard(
                    signal = signal,
                    isSelected = false,
                    onCardClick = { onInspectSignal(signal) },
                    onToggleSave = { onRemoveSaved(signal.id) },
                    onSizeTrade = { onSizeTrade(signal) },
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }
        }

        item(key = "methodology_card") {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, tradingColors.cardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = tradingColors.accentCyan
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.disclaimer_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.disclaimer_body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
