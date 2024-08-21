package dev.sanskar.featuretesteduco.feature_course.data.remote

import dev.sanskar.featuretesteduco.core.data.dto.response.BasicApiResponse
import dev.sanskar.featuretesteduco.core.data.dto.response.CoursesResponse
import dev.sanskar.featuretesteduco.core.data.dto.response.UserItemDto
import dev.sanskar.featuretesteduco.core.domain.models.*
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.feature_course.data.remote.dto.CommentDto
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCommentRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.LikeUpdateRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface CourseApi {

    //Search for Courses


    @GET("/api/user/search/courses")
    suspend fun searchCourses(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): BasicApiResponse<List<CourseOverview>>

    //Bookmark
    @GET("/api/user/bookmark/courses")
    suspend fun getBookmarkedCourses(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): BasicApiResponse<List<CourseOverview>>

    @GET("/api/tchr/courses")
    suspend fun getCoursesforTeacherPaged(
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        @Query("Teacherid") Teacherid: String
    ): BasicApiResponse<List<CourseOverview>>

    @GET("/api/user/profile/courses")
    suspend fun getCoursesForProfile(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userId") userId: String,
    ): CoursesResponse



    @POST("/api/user/bookmark/create")
    suspend fun createBookmark(
        @Query("userId") userId: String,
        @Query("courseId") courseId: String
    ): BasicApiResponse<Unit>

    @Multipart
    @POST("/api/user/tutor/create_course")
    suspend fun createCourse(
        @Part() CreateCourseRequest: MultipartBody.Part,
        @Part() thumbnailuri: MultipartBody.Part?,
        @Part() introvideouri: MultipartBody.Part?
    ): BasicApiResponse<Unit>
    @Multipart
    @POST("/api/user/course/create_lesson")
    suspend fun createLessonForCourse(
        @Query("courseId") courseId: String,
        @Part() CreateLessonRequest: MultipartBody.Part,
        @Part() thumbnailuri: MultipartBody.Part?,
        @Part() introvideouri: MultipartBody.Part?
    ): BasicApiResponse<Unit>

    @Multipart
    @POST("/api/user/course/create_resource")
    suspend fun createCourseResource(
        @Query("courseId") courseId: String,

        @Part() thumbnailuri: List<MultipartBody.Part>?,

    ): BasicApiResponse<Unit>



    @GET("/api/get_discover_course")
    suspend fun getDiscoverCourses(
        @Query("page") page: Int,
        @QueryMap options: Map<String, String>
    ): CoursesResponse

    //Feed
    @GET("/api/course/popular_categories")
    suspend fun getPopularCategories(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<CateforyOverView>
    @DELETE("/api/user/tutor/delete_course")
    suspend fun deleteCourse(
        @Query("courseId") courseId: String
    ): BasicApiResponse<Unit>

    @GET("/api/course/most_watched_courses")
    suspend fun getMostWatchedCourses(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<CourseOverview>

    @GET("/api/course/previous_watched_courses")
    suspend fun getPreviousWatchedCourses(
        @Query("studentId") studentId: String,

        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<CourseOverview>

    @GET("/api/course/others_watched_courses")
    suspend fun getOthersWatchedCourses(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<CourseOverview>


    //Course
    @GET("/api/course/details")
    suspend fun getCourseDetails(
        @Query("courseId") courseId: String
    ): BasicApiResponse<Course>

    @GET("/api/user/course/comments")
    suspend fun getCommentsForCourse(
        @Query("courseId") courseId: String
    ): BasicApiResponse<List<CommentDto>>

    @GET("/api/user/course/projects")
    suspend fun getProjectsForCourse(
        @Query("courseId") courseId: String
    ): BasicApiResponse<List<Project>>

    @GET("/api/user/course/resources")
    suspend fun getResourcesForCourse(
        @Query("courseId") courseId: String
    ): BasicApiResponse<List<CourseResource>>
    @GET("/api/user/resources/lesson")
    suspend fun getResourcesForCourseLesson(

        @Query("lessonId") lessonId: String
    ): BasicApiResponse<List<CourseResource>>

    @GET("/api/user/course/lessons")
    suspend fun getLessonsForCourse(
        @Query("courseId") courseId: String
    ): BasicApiResponse<List<Lesson>>

    @POST("/api/comment/create")
    suspend fun createComment(
        @Body request: CreateCommentRequest
    ): BasicApiResponse<Unit>

    @POST("/api/like")
    suspend fun likeParent(
        @Body request: LikeUpdateRequest
    ): BasicApiResponse<Unit>

    @DELETE("/api/unlike")
    suspend fun unlikeParent(
        @Query("parentId") parentId: String,
        @Query("parentType") parentType: Int
    ): BasicApiResponse<Unit>

    @GET("/api/like/parent")
    suspend fun getLikesForParent(
        @Query("courseId") parentId: String
    ): List<UserItemDto>

    @GET("/api/user/course/lesson")
    suspend fun getLessonDetails(
        @Query("lessonId") lessonId: String
    ): BasicApiResponse<Lesson>
    @GET("api/tchr/highly/rated/courses")
    suspend fun getRatedCrs(
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        @Query("Teacherid") Teacherid: String

    ):BasicApiResponse<List<CourseOverview>>
    @GET("api/tchr/highly/enrolled/courses")
    suspend fun getEnrolledCrs(
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        @Query("Teacherid") Teacherid: String

    ):BasicApiResponse<List<CourseOverview>>




    companion object {
        const val BASE_URL = "http://192.168.0.120:8082"
    }
}