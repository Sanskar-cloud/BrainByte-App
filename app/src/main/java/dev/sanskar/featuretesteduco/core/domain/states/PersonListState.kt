package dev.sanskar.featuretesteduco.core.domain.states

import dev.sanskar.featuretesteduco.core.domain.models.UserItem

data class PersonListState(
    val users: List<UserItem> = emptyList(),
    val isLoading: Boolean = false
)
