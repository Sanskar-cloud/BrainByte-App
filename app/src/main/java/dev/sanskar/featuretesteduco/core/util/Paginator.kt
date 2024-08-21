package dev.sanskar.featuretesteduco.core.util

interface Paginator<T> {

    suspend fun loadNextItems()
}