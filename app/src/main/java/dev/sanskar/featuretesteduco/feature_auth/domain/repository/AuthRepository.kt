package dev.sanskar.featuretesteduco.feature_auth.domain.repository

import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.AccountDetails

interface AuthRepository {

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
        accountType: String,
        accountDetails: AccountDetails?=null,
    ): SimpleResource



    suspend fun signIn(
        email: String,
        password: String
    ): SimpleResource

    suspend fun resetPassword(
        oldPassword: String,
        newPassword: String
    ): SimpleResource

    suspend fun authenticate(): SimpleResource

}