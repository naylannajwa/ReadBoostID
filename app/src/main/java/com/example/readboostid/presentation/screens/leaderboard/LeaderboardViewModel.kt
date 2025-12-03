// File: presentation/screens/leaderboard/LeaderboardViewModel.kt
package com.readboost.id.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readboost.id.data.model.Leaderboard
import com.readboost.id.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LeaderboardUiState(
    val leaderboard: List<Leaderboard> = emptyList(),
    val isLoading: Boolean = true
)

class LeaderboardViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            userDataRepository.getAllLeaderboard()
                .collect { leaderboard ->
                    _uiState.update { it.copy(leaderboard = leaderboard, isLoading = false) }
                }
        }
    }
}