package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_profile.domain.model.ProfileHeader
import javax.inject.Inject

class GetProfileHeaderUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(userId: String): Resource<ProfileHeader> {
        return repository.getProfileHeader(userId)
    }
}