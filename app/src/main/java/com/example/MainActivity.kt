package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppTab
import com.example.ui.screens.PositionSizerScreen
import com.example.ui.screens.SignalsScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.IndexPulseTheme
import com.example.ui.theme.LocalTradingColors
import com.example.ui.viewmodel.SignalsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndexPulseApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexPulseApp(
    viewModel: SignalsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showMethodologyDialog by remember { mutableStateOf(false) }

    // Support system back gesture from secondary tabs back to Signals
    if (uiState.selectedTab != AppTab.SIGNALS) {
        BackHandler {
            viewModel.selectTab(AppTab.SIGNALS)
        }
    }

    IndexPulseTheme(darkTheme = uiState.isDarkTheme) {
        val tradingColors = LocalTradingColors.current

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavRail = maxWidth >= 840.dp

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CandlestickChart,
                                    contentDescription = null,
                                    tint = tradingColors.bullish,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.refreshSignals() },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("topbar_refresh_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.action_refresh)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleTheme() },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("topbar_theme_toggle")
                            ) {
                                Icon(
                                    imageVector = if (uiState.isDarkTheme) {
                                        Icons.Filled.LightMode
                                    } else {
                                        Icons.Filled.DarkMode
                                    },
                                    contentDescription = if (uiState.isDarkTheme) {
                                        "Switch to daylight theme"
                                    } else {
                                        "Switch to obsidian dark theme"
                                    }
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                bottomBar = {
                    if (!useNavRail) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            AppTab.entries.forEach { tab ->
                                val selected = uiState.selectedTab == tab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        TabIcon(
                                            tab = tab,
                                            selected = selected,
                                            savedCount = uiState.savedSignals.size
                                        )
                                    },
                                    label = { Text(tab.label) },
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    if (uiState.selectedTab == AppTab.SIGNALS) {
                        ExtendedFloatingActionButton(
                            onClick = { viewModel.refreshSignals() },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null
                                )
                            },
                            text = { Text(stringResource(R.string.action_refresh)) },
                            modifier = Modifier.testTag("fab_refresh_signals")
                        )
                    }
                }
            ) { innerPadding ->
                Row(modifier = Modifier.fillMaxSize()) {
                    if (useNavRail) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = innerPadding.calculateTopPadding())
                        ) {
                            AppTab.entries.forEach { tab ->
                                val selected = uiState.selectedTab == tab
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        TabIcon(
                                            tab = tab,
                                            selected = selected,
                                            savedCount = uiState.savedSignals.size
                                        )
                                    },
                                    label = { Text(tab.label) },
                                    modifier = Modifier.testTag("rail_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }

                    Crossfade(
                        targetState = uiState.selectedTab,
                        label = "tab_crossfade",
                        modifier = Modifier.weight(1f)
                    ) { activeTab ->
                        when (activeTab) {
                            AppTab.SIGNALS -> {
                                SignalsScreen(
                                    uiState = uiState,
                                    onRefreshAll = { viewModel.refreshSignals() },
                                    onRegenerateIndex = { viewModel.regenerateSingleIndex(it) },
                                    onSelectFilter = { viewModel.selectFilter(it) },
                                    onInspectSignal = { viewModel.inspectSignal(it) },
                                    onToggleSave = { viewModel.toggleSaveSignal(it) },
                                    onSizeTrade = { viewModel.loadSignalIntoSizer(it) },
                                    onOpenMethodologyDialog = { showMethodologyDialog = true },
                                    contentPadding = innerPadding
                                )
                            }

                            AppTab.SIZER -> {
                                PositionSizerScreen(
                                    sizing = uiState.positionSizing,
                                    onIndexSelected = { viewModel.updateSizerIndex(it) },
                                    onLotsChanged = { viewModel.updateSizerLots(it) },
                                    onCapitalChanged = { viewModel.updateSizerCapital(it) },
                                    onEntryChanged = { viewModel.updateSizerEntry(it) },
                                    contentPadding = innerPadding
                                )
                            }

                            AppTab.SAVED -> {
                                WatchlistScreen(
                                    savedSignals = uiState.savedSignals,
                                    onInspectSignal = { signal ->
                                        viewModel.inspectSignal(signal)
                                        viewModel.selectTab(AppTab.SIGNALS)
                                    },
                                    onRemoveSaved = { viewModel.removeSavedSignal(it) },
                                    onClearAll = { viewModel.clearSavedSignals() },
                                    onSizeTrade = { viewModel.loadSignalIntoSizer(it) },
                                    onBrowseSignals = { viewModel.selectTab(AppTab.SIGNALS) },
                                    contentPadding = innerPadding
                                )
                            }
                        }
                    }
                }
            }

            if (showMethodologyDialog) {
                AlertDialog(
                    onDismissRequest = { showMethodologyDialog = false },
                    title = { Text(stringResource(R.string.disclaimer_title)) },
                    text = {
                        Text(
                            text = "${stringResource(R.string.disclaimer_body)}\n\n" +
                                "Indices tracked: NIFTY 50 (NSE), BANK NIFTY (NSE), SENSEX (BSE), and FINNIFTY (NSE). " +
                                "All signals are generated locally in simulation mode until connected to a live broker feed."
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showMethodologyDialog = false },
                            modifier = Modifier.testTag("methodology_dialog_ok")
                        ) {
                            Text("Got It")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TabIcon(
    tab: AppTab,
    selected: Boolean,
    savedCount: Int
) {
    when (tab) {
        AppTab.SIGNALS -> Icon(
            imageVector = if (selected) Icons.Filled.CandlestickChart else Icons.Outlined.CandlestickChart,
            contentDescription = tab.label
        )

        AppTab.SIZER -> Icon(
            imageVector = if (selected) Icons.Filled.Calculate else Icons.Outlined.Calculate,
            contentDescription = tab.label
        )

        AppTab.SAVED -> BadgedBox(
            badge = {
                if (savedCount > 0) {
                    Badge { Text(savedCount.toString()) }
                }
            }
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = tab.label
            )
        }
    }
}
