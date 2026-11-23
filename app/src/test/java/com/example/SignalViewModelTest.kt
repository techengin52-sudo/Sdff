package com.example

import com.example.data.SignalRepository
import com.example.model.AppTab
import com.example.model.IndexSpec
import com.example.model.OptionType
import com.example.model.SignalDirection
import com.example.model.SignalFilter
import com.example.ui.viewmodel.SignalsViewModel
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignalViewModelTest {

    @Test
    fun repositoryGeneratesValidTargetsAndStopLossForAllIndices() {
        val repo = SignalRepository(Random(42))
        val signals = repo.generateBatch()

        assertEquals(IndexSpec.entries.size, signals.size)
        signals.forEach { signal ->
            assertTrue(signal.entry in 140.0..320.0)
            assertTrue(signal.target1 > signal.entry)
            assertTrue(signal.target2 > signal.target1)
            assertTrue(signal.stopLoss < signal.entry)
            assertTrue(signal.confidenceScore in 82..95)
            if (signal.direction == SignalDirection.STRONG_BUY) {
                assertEquals(OptionType.CE, signal.optionType)
                assertEquals("STRONG BUY", signal.signalType)
            } else {
                assertEquals(OptionType.PE, signal.optionType)
                assertEquals("STRONG SELL", signal.signalType)
            }
        }
    }

    @Test
    fun viewModelBookmarkAndSizerIntegrationWorks() {
        val viewModel = SignalsViewModel(SignalRepository(Random(7)))
        val initialSignals = viewModel.uiState.value.signals
        assertEquals(4, initialSignals.size)

        val target = initialSignals.first()
        viewModel.toggleSaveSignal(target)
        assertEquals(1, viewModel.uiState.value.savedSignals.size)

        viewModel.selectFilter(SignalFilter.SAVED)
        assertEquals(1, viewModel.uiState.value.filteredSignals.size)

        viewModel.loadSignalIntoSizer(target)
        assertEquals(AppTab.SIZER, viewModel.uiState.value.selectedTab)
        assertEquals(target.entry, viewModel.uiState.value.positionSizing.entryPrice, 0.01)
    }
}
