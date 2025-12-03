// File: presentation/screens/home/HomeViewModel.kt
package com.readboost.id.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readboost.id.data.model.Article
import com.readboost.id.data.model.UserProgress
import com.readboost.id.domain.repository.ArticleRepository
import com.readboost.id.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val userProgress: UserProgress? = null,
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val articleRepository: ArticleRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                articleRepository.getAllArticles(),
                userDataRepository.getUserProgress()
            ) { articles, progress ->
                HomeUiState(
                    articles = articles.take(5), // Show only recent 5 articles
                    userProgress = progress,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}