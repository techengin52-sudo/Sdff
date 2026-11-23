package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.SignalRepository
import com.example.model.AppTab
import com.example.model.IndexSpec
import com.example.model.OptionSignal
import com.example.model.PositionSizingState
import com.example.model.SignalDirection
import com.example.model.SignalFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.round

data class SignalsUiState(
    val signals: List<OptionSignal> = emptyList(),
    val savedSignals: List<OptionSignal> = emptyList(),
    val selectedFilter: SignalFilter = SignalFilter.ALL,
    val selectedTab: AppTab = AppTab.SIGNALS,
    val inspectedSignal: OptionSignal? = null,
    val lastUpdatedTime: String = "",
    val refreshCount: Int = 0,
    val isDarkTheme: Boolean = true,
    val positionSizing: PositionSizingState = PositionSizingState()
) {
    val filteredSignals: List<OptionSignal>
        get() = when (selectedFilter) {
            SignalFilter.ALL -> signals
            SignalFilter.STRONG_BUY -> signals.filter { it.direction == SignalDirection.STRONG_BUY }
            SignalFilter.STRONG_SELL -> signals.filter { it.direction == SignalDirection.STRONG_SELL }
            SignalFilter.SAVED -> savedSignals
        }

    val bullishCount: Int get() = signals.count { it.direction == SignalDirection.STRONG_BUY }
    val bearishCount: Int get() = signals.count { it.direction == SignalDirection.STRONG_SELL }
    val averageConfidence: Int
        get() = if (signals.isEmpty()) 0 else signals.sumOf { it.confidenceScore } / signals.size
}

class SignalsViewModel(
    private val repository: SignalRepository = SignalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalsUiState())
    val uiState: StateFlow<SignalsUiState> = _uiState.asStateFlow()

    init {
        refreshSignals()
    }

    fun refreshSignals() {
        val freshBatch = repository.generateBatch()
        val updatedTime = freshBatch.firstOrNull()?.generatedTime.orEmpty()
        _uiState.update { state ->
            val firstSignal = freshBatch.firstOrNull()
            val nextSizing = if (state.refreshCount == 0 && firstSignal != null) {
                PositionSizingState(
                    selectedIndex = firstSignal.indexSpec,
                    strikeLabel = firstSignal.strike,
                    entryPrice = firstSignal.entry,
                    target1Price = firstSignal.target1,
                    target2Price = firstSignal.target2,
                    stopLossPrice = firstSignal.stopLoss
                )
            } else {
                state.positionSizing
            }
            state.copy(
                signals = freshBatch,
                lastUpdatedTime = updatedTime,
                refreshCount = state.refreshCount + 1,
                inspectedSignal = state.inspectedSignal?.let { current ->
                    freshBatch.find { it.indexSpec == current.indexSpec } ?: current
                },
                positionSizing = nextSizing
            )
        }
    }

    fun regenerateSingleIndex(indexSpec: IndexSpec) {
        val regenerated = repository.generateSignal(indexSpec)
        _uiState.update { state ->
            val updatedList = state.signals.map { existing ->
                if (existing.indexSpec == indexSpec) regenerated else existing
            }
            state.copy(
                signals = updatedList,
                lastUpdatedTime = regenerated.generatedTime,
                inspectedSignal = if (state.inspectedSignal?.indexSpec == indexSpec) regenerated else state.inspectedSignal
            )
        }
    }

    fun selectFilter(filter: SignalFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun inspectSignal(signal: OptionSignal?) {
        _uiState.update { it.copy(inspectedSignal = signal) }
    }

    fun toggleSaveSignal(signal: OptionSignal) {
        _uiState.update { state ->
            val alreadySaved = state.savedSignals.any { it.id == signal.id }
            val updatedSavedList = if (alreadySaved) {
                state.savedSignals.filterNot { it.id == signal.id }
            } else {
                listOf(signal.copy(isSaved = true)) + state.savedSignals
            }
            val updatedSignals = state.signals.map { item ->
                if (item.id == signal.id) item.copy(isSaved = !alreadySaved) else item
            }
            val updatedInspected = if (state.inspectedSignal?.id == signal.id) {
                state.inspectedSignal.copy(isSaved = !alreadySaved)
            } else {
                state.inspectedSignal
            }
            state.copy(
                signals = updatedSignals,
                savedSignals = updatedSavedList,
                inspectedSignal = updatedInspected
            )
        }
    }

    fun removeSavedSignal(signalId: String) {
        _uiState.update { state ->
            state.copy(
                savedSignals = state.savedSignals.filterNot { it.id == signalId },
                signals = state.signals.map { if (it.id == signalId) it.copy(isSaved = false) else it },
                inspectedSignal = if (state.inspectedSignal?.id == signalId) {
                    state.inspectedSignal.copy(isSaved = false)
                } else {
                    state.inspectedSignal
                }
            )
        }
    }

    fun clearSavedSignals() {
        _uiState.update { state ->
            state.copy(
                savedSignals = emptyList(),
                signals = state.signals.map { it.copy(isSaved = false) },
                inspectedSignal = state.inspectedSignal?.copy(isSaved = false)
            )
        }
    }

    fun loadSignalIntoSizer(signal: OptionSignal) {
        _uiState.update { state ->
            state.copy(
                positionSizing = state.positionSizing.copy(
                    selectedIndex = signal.indexSpec,
                    strikeLabel = signal.strike,
                    entryPrice = signal.entry,
                    target1Price = signal.target1,
                    target2Price = signal.target2,
                    stopLossPrice = signal.stopLoss
                ),
                inspectedSignal = null,
                selectedTab = AppTab.SIZER
            )
        }
    }

    fun updateSizerIndex(indexSpec: IndexSpec) {
        _uiState.update { state ->
            state.copy(
                positionSizing = state.positionSizing.copy(
                    selectedIndex = indexSpec,
                    strikeLabel = "${indexSpec.baseSpot} CE"
                )
            )
        }
    }

    fun updateSizerLots(lots: Int) {
        val clamped = lots.coerceIn(1, 50)
        _uiState.update { state ->
            state.copy(positionSizing = state.positionSizing.copy(lotsCount = clamped))
        }
    }

    fun updateSizerCapital(capital: Double) {
        val clamped = capital.coerceIn(5000.0, 5000000.0)
        _uiState.update { state ->
            state.copy(positionSizing = state.positionSizing.copy(capitalAmount = clamped))
        }
    }

    fun updateSizerEntry(entry: Double) {
        val sanitized = round((entry.coerceIn(5.0, 5000.0)) * 10.0) / 10.0
        val t1 = round((sanitized * 1.15) * 10.0) / 10.0
        val t2 = round((sanitized * 1.30) * 10.0) / 10.0
        val sl = round((sanitized * 0.85) * 10.0) / 10.0
        _uiState.update { state ->
            state.copy(
                positionSizing = state.positionSizing.copy(
                    entryPrice = sanitized,
                    target1Price = t1,
                    target2Price = t2,
                    stopLossPrice = sl
                )
            )
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }
}
