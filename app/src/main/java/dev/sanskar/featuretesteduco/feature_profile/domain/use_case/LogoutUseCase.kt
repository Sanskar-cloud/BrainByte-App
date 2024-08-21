package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository

class LogoutUseCase(
    private val repository: ProfileRepository
) {

    operator fun invoke() {
        repository.logout()
    }
}