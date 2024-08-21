package dev.sanskar.featuretesteduco.feature_course.domain.repository

import android.content.Context
import android.net.Uri
import androidx.paging.PagingData
import dev.sanskar.featuretesteduco.core.domain.models.*
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateLessonRequest
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    suspend fun searchCourses(query: String, page: Int): Resource<List<CourseOverview>>
    suspend fun searchCourses2(query: String, page: Int): List<Course>?
    suspend fun createCourse(createCourseRequest: CreateCourseRequest, thumbnailuri: Uri?,
                             introvideouri: Uri?, context: Context
    ): SimpleResource

                             suspend fun getBookmarkedCourses(userId: String, page: Int, pageSize: Int): Resource<List<CourseOverview>>

    suspend fun createCourseResource(courseId: String, thumbnailuri: List<Uri>?, context: Context): SimpleResource

    suspend fun AddToBookmarkedCourses(userId: String, courseId: String): SimpleResource

    suspend fun getPopularCategories(page: Int, pageSize: Int): Resource<List<CateforyOverView>>

    suspend fun getMostWatchedCourses(page: Int, pageSize: Int): Resource<List<CourseOverview>>

    suspend fun getPreviousWatchedCourses(studentId: String, page: Int, pageSize: Int): Resource<List<CourseOverview>>

    suspend fun getOthersWatchedCourses(page: Int, pageSize: Int): Resource<List<CourseOverview>>


    suspend fun getDiscoverCourses(filterResult: FilterResult?): Flow<PagingData<CourseOverview>>

    suspend fun getCourseDetails(courseId: String): Resource<Course?>

    suspend fun getCommentsForCourse(courseId: String): Resource<List<Comment>>
    suspend fun getLessonsForCourse(courseId: String): Resource<List<Lesson>>
    suspend fun getProjectsForCourse(courseId: String): Resource<List<Project>>
    suspend fun getCoursesforTeacherPaged(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        Teacherid: String
    ): Resource<List<CourseOverview>>
    suspend fun getHighlyRatedCrs(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        Teacherid: String
    ): Resource<List<CourseOverview>>
    suspend fun getHighlyEnrolledCrs(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        Teacherid: String
    ): Resource<List<CourseOverview>>
    suspend fun getResourcesForCourse(courseId: String): Resource<List<CourseResource>>
    suspend fun getResourcesForCourseLesson( lessonId: String): Resource<List<CourseResource>>

    suspend fun createComment(courseId: String, comment: String): SimpleResource
    suspend fun createLesson(courseId: String, createLessonRequest: CreateLessonRequest,
                             thumbnailuri: Uri?,
                             videouri: Uri?,
                             context: Context): SimpleResource

    suspend fun likeParent(parentId: String, parentType: Int,userId: String): SimpleResource

    suspend fun unlikeParent(parentId: String, parentType: Int): SimpleResource

    suspend fun getLikesForParent(parentId: String): Resource<List<UserItem>>
    suspend fun getLessonDetails(lessonId: String): Resource<Lesson?>

    suspend fun deleteCourse(courseId: String): SimpleResource

}