package dev.sanskar.featuretesteduco.feature_auth.data.repository

import android.content.SharedPreferences
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_auth.data.remote.AuthApi
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.AccountDetails
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.CreateAccountRequest
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.ResetPasswordRequest
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.SignInRequest
import dev.sanskar.featuretesteduco.feature_auth.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
        accountType: String,
        accountDetails: AccountDetails?
    ): SimpleResource {
        val request = CreateAccountRequest(email, username, password, accountType, accountDetails)
        return try {
            val response = api.signUp(request)
            if(response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch(e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }







    override suspend fun signIn(email: String, password: String): SimpleResource {
        val request = SignInRequest(email, password)
        return try {
            val response = api.signIn(request)
            if(response.successful) {
                response.data?.let { authResponse ->
                    println("Overriding token with ${authResponse.token}")
                    println("User ID ${authResponse.userId}")
                    sharedPreferences.edit()
                        .putString(Constants.KEY_JWT_TOKEN, authResponse.token)
                        .putString(Constants.KEY_USER_ID, authResponse.userId)
                        .putString(Constants.KEY_ACCOUNT_TYPE, authResponse.userType)
                        .apply()
                }
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch(e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun resetPassword(oldPassword: String, newPassword: String): SimpleResource {
        val request = ResetPasswordRequest(oldPassword, newPassword)
        return try {
            val response = api.resetPassword(request)
            if(response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch(e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun authenticate(): SimpleResource {
        return try {
            api.authenticate()
            Resource.Success(Unit)
        } catch(e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }
}

