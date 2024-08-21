package dev.sanskar.featuretesteduco.core.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.SimpleResource

class ToggleFollowStateForUserUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(followedUserId: String, isFollowing: Boolean): SimpleResource {
        return if(isFollowing == true) {
            repository.unfollowUser(followedUserId)
        } else {
            repository.followUser(followedUserId)
        }
    }
}