package dev.sanskar.featuretesteduco.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import coil.ImageLoader
import coil.decode.SvgDecoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateLessonUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            Constants.SHARED_PREF_NAME,
            MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS) // Example: 30 seconds
            .readTimeout(45, TimeUnit.SECONDS)    // Example: 30 seconds
            .writeTimeout(45, TimeUnit.SECONDS)
            .addInterceptor {
                val token = sharedPreferences.getString(Constants.KEY_JWT_TOKEN, "")
                val modifiedRequest = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                it.proceed(modifiedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader.Builder(app)
            .crossfade(true)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGetOwnUserIdUseCase(sharedPreferences: SharedPreferences): GetOwnUserIdUseCase {
        return GetOwnUserIdUseCase(sharedPreferences)
    }
    @Provides
    @Singleton
    fun provideCreateLessonUseCase(repository: CourseRepository): CreateLessonUseCase {
        return CreateLessonUseCase(repository)
    }

}