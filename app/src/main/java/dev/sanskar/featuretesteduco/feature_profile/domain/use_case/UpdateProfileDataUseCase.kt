package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import android.content.Context
import android.net.Uri
import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_auth.domain.models.SignInResult
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData
import dev.sanskar.featuretesteduco.feature_settings.presentation.UpdateProfileResult

class UpdateProfileDataUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(
        updateProfileData: UpdateProfileData,
        bannerImageUri: Uri?,
        profilePictureUri: Uri?,
        context: Context
    ): UpdateProfileResult{
        return UpdateProfileResult(
            result = profileRepository.updateProfile(
                updateProfileData,
                bannerImageUri,
                profilePictureUri,
                context
            )
        )

    }
}