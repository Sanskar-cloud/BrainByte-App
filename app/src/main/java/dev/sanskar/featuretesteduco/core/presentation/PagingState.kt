package dev.sanskar.featuretesteduco.core.presentation

data class PagingState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false
)