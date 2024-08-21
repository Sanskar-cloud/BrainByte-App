package dev.sanskar.featuretesteduco.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sanskar.featuretesteduco.core.data.source.remote.RemoteDataSourceImpl
import dev.sanskar.featuretesteduco.core.domain.source.DataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): DataSource.Remote

}