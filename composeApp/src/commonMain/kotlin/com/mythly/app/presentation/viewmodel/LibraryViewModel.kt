package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.usecase.FilterStoriesUseCase
import com.mythly.app.domain.usecase.GetAllStoriesUseCase
import com.mythly.app.domain.usecase.SearchStoriesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class LibraryFilter {
    data object All : LibraryFilter()
    data class ByDeity(val deity: Deity) : LibraryFilter()
    data class ByEpic(val epic: Epic) : LibraryFilter()
    data object Read : LibraryFilter()
    data object Favorites : LibraryFilter()
}

data class LibraryUiState(
    val isLoading: Boolean = true,
    val stories: List<StoryUiState> = emptyList(),
    val filteredStories: List<StoryUiState> = emptyList(),
    val searchQuery: String = "",
    val currentFilter: LibraryFilter = LibraryFilter.All,
    val error: String? = null
)

class LibraryViewModel(
    private val getAllStoriesUseCase: GetAllStoriesUseCase,
    private val searchStoriesUseCase: SearchStoriesUseCase,
    private val filterStoriesUseCase: FilterStoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadAllStories()
    }

    private fun loadAllStories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getAllStoriesUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to load stories"
                        )
                    }
                }
                .collectLatest { stories ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            stories = stories,
                            filteredStories = applyCurrentFilter(stories, it.currentFilter, it.searchQuery)
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }

            if (query.isBlank()) {
                // Show filtered stories when search is empty
                val currentState = _uiState.value
                _uiState.update {
                    it.copy(
                        filteredStories = applyCurrentFilter(
                            currentState.stories,
                            currentState.currentFilter,
                            ""
                        )
                    )
                }
            } else {
                // Perform search
                searchStoriesUseCase(query)
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(error = throwable.message ?: "Search failed")
                        }
                    }
                    .collectLatest { searchResults ->
                        _uiState.update { it.copy(filteredStories = searchResults) }
                    }
            }
        }
    }

    fun onFilterChange(filter: LibraryFilter) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentFilter = filter) }

            val currentState = _uiState.value
            val filtered = applyCurrentFilter(
                currentState.stories,
                filter,
                currentState.searchQuery
            )
            _uiState.update { it.copy(filteredStories = filtered) }
        }
    }

    private fun applyCurrentFilter(
        stories: List<StoryUiState>,
        filter: LibraryFilter,
        searchQuery: String
    ): List<StoryUiState> {
        if (searchQuery.isNotBlank()) {
            // If search is active, search takes precedence
            return stories
        }

        return when (filter) {
            is LibraryFilter.All -> stories
            is LibraryFilter.ByDeity -> stories.filter { it.story.deity == filter.deity }
            is LibraryFilter.ByEpic -> stories.filter { it.story.epic == filter.epic }
            is LibraryFilter.Read -> stories.filter { it.isRead }
            is LibraryFilter.Favorites -> stories.filter { it.isFavorite }
        }
    }

    fun retry() {
        loadAllStories()
    }
}
