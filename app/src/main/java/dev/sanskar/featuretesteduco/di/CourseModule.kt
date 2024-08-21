package dev.sanskar.featuretesteduco.di

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.feature_course.data.remote.CourseApi
import dev.sanskar.featuretesteduco.feature_course.data.repository.CourseRepositoryImpl
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateLessonUseCase
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideCourseApi(client: OkHttpClient): CourseApi {

        val contentType = "application/json".toMediaType()

        return  Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl(CourseApi.BASE_URL)
            .client(client)
            .build()
            .create(CourseApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideCourseRepository(
//        api: CourseApi,
//    ): CourseRepository {
//        return CourseRepositoryImpl(api)
//    }



    /*@Provides
    @Singleton
    fun provideCourseUseCases(repository: CourseRepository): CourseUseCases {
        return CourseUseCases(
            getCategories = GetPopularCategoriesUseCase(repository),
            getMostWatchedCourseUseCase = GetMostWatchedCourseUseCase(repository),
            getPreviousWatchedCoursesUseCase = GetPreviousWatchedCoursesUseCase(repository),
            getOthersWatchedCourses = GetOthersWatchedCourses(repository),
            getCoursesForFollows = GetCoursesForFollowsUseCase(repository),
            getCourseDetails = GetCourseDetailsUseCase(repository),
            getCommentsForCourse = GetCommentsForCourseUseCase(repository),
            createComment = CreateCommentUseCase(repository),
            toggleLikeForParent = ToggleLikeForParentUseCase(repository),
            getLikesForParent = GetLikesForParentUseCase(repository),
            getLessonsForCourse = GetLessonsForCourse(repository),
            getProjectsForCourse = GetProjectsForCourse(repository),
            getResourcesForCourse = GetResourcesForCourse(repository),
        )
    }*/

}