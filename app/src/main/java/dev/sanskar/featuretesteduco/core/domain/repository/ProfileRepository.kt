package dev.sanskar.featuretesteduco.core.domain.repository

import android.content.Context
import android.net.Uri
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.models.UserItem
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_profile.domain.model.Profile
import dev.sanskar.featuretesteduco.feature_profile.domain.model.ProfileHeader
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData

interface ProfileRepository {

    suspend fun getCoursesPaged(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        userId: String
    ): Resource<List<CourseOverview>>

    suspend fun getUserInfos(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
    ): Resource<List<UserItem>>

    suspend fun getProfile(userId: String): Resource<Profile>

    suspend fun getProfileHeader(userId: String): Resource<ProfileHeader>

    suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        bannerImageUri: Uri?,
        profilePictureUri: Uri?,
        context: Context
    ): SimpleResource


    suspend fun searchUser(query: String): Resource<List<UserItem>>

    suspend fun followUser(followedUserId: String): SimpleResource

    suspend fun unfollowUser(followedUserId: String): SimpleResource

    fun logout()
}