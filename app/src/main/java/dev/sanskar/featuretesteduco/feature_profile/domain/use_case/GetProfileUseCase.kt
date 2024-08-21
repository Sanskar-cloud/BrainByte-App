package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_profile.domain.model.Profile

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Resource<Profile> {
        return repository.getProfile(userId)
    }

}