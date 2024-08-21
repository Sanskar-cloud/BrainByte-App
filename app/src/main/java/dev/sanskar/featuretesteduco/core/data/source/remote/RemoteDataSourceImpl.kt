package dev.sanskar.featuretesteduco.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.models.FilterResult
import dev.sanskar.featuretesteduco.core.domain.source.DataSource
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.feature_course.data.paging.CourseSource
import dev.sanskar.featuretesteduco.feature_course.data.remote.CourseApi
import dev.sanskar.featuretesteduco.feature_course.presentation.util.CourseRequestOptionsMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val courseApi: CourseApi,
    private val courseRequestOptionsMapper: CourseRequestOptionsMapper
): DataSource.Remote {
    override suspend fun getDiscoverCourses(filterResult: FilterResult?): Flow<PagingData<CourseOverview>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGE_SIZE,
                prefetchDistance = 2,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                jumpThreshold = Int.MIN_VALUE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CourseSource(
                    api = courseApi,
                    source = CourseSource.Source.DiscoverCourses,
                    courseRequestOptionsMapper = courseRequestOptionsMapper,
                    filterResult = filterResult
                )
            }
        ).flow

}