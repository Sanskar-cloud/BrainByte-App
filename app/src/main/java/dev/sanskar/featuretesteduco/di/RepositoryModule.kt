package dev.sanskar.featuretesteduco.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sanskar.featuretesteduco.feature_course.data.repository.CourseRepositoryImpl
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideCourseRepository(courseRepositoryImpl: CourseRepositoryImpl): CourseRepository
}