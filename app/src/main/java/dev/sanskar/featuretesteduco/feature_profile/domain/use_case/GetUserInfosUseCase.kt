package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.UserItem
import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Resource

class GetUserInfosUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): Resource<List<UserItem>> {
        return profileRepository.getUserInfos(page = page, pageSize = pageSize)
    }
}