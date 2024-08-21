package dev.sanskar.featuretesteduco.feature_course.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import dev.sanskar.featuretesteduco.core.domain.models.*
import androidx.paging.PagingData
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.source.DataSource
import dev.sanskar.featuretesteduco.core.domain.util.getFileName
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.data.remote.CourseApi
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCommentRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateLessonRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.LikeUpdateRequest
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val remote: DataSource.Remote,
    private val api: CourseApi,
): CourseRepository {
    override suspend fun searchCourses(query: String, page: Int): Resource<List<CourseOverview>> {
        return try {
            val courses = api.searchCourses(
                query = query,
                page = 0,
            ).data
            Resource.Success(data = courses)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun searchCourses2(query: String, page: Int): List<Course>? {
        TODO("Not yet implemented")
    }

//    override suspend fun searchCourses2(query: String, page: Int): List<Course>? {
//        return api.searchCourses(
//            query = query,
//            page = 0,
//        ).data
//
//    }


    override suspend fun createCourse(
        createCourseRequest: CreateCourseRequest,
        thumbnailuri: Uri?,
        introvideouri: Uri?,
        context: Context
    ): SimpleResource {
        val contentResolver = context.contentResolver
        val thumbnailFile: File? = thumbnailuri?.let {
            val fileName = contentResolver.getFileName(it)
            val file = File(context.cacheDir, fileName)
            try {
                val inputStream = contentResolver.openInputStream(it)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        val introVideoFile: File? = introvideouri?.let {
            val fileName = contentResolver.getFileName(it)
            val file = File(context.cacheDir, fileName)
            try {
                val inputStream = contentResolver.openInputStream(it)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }


//        val thumbnailFile = Uri.fromFile(
//            File(
//                context.cacheDir,
//                thumbnailuri?.let { context.contentResolver.getFileName(it) }
//            )
//        ).toFile()
//        val introVideoFile = Uri.fromFile(
//            File(
//                context.cacheDir,
//                introvideouri?.let { context.contentResolver.getFileName(it) }
//            )
//        ).toFile()

        return try {
            val response = api.createCourse(
                CreateCourseRequest = MultipartBody.Part
                    .createFormData(
                        "course_data",
                        Json.encodeToString(createCourseRequest)

            ),
                thumbnailuri = thumbnailFile?.let {
                    MultipartBody.Part
                        .createFormData(
                            "course_thumbnail",
                            thumbnailFile.name,
                            thumbnailFile.asRequestBody()
                        )
                },
                introvideouri = introVideoFile?.let {
                    MultipartBody.Part
                        .createFormData(
                            "course_intro_video",
                            introVideoFile.name,
                            introVideoFile.asRequestBody()
                        )
                },

            )
            if(response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        }  catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }




        }




    override suspend fun getBookmarkedCourses(userId: String, page: Int, pageSize: Int): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getBookmarkedCourses(
                page = page,
                pageSize = pageSize,
                userId = userId
            ).data
            Resource.Success(data = courses)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun createCourseResource(
        courseId: String,
        thumbnailuri: List<Uri>?,
        context: Context
    ): SimpleResource {
        val contentResolver = context.contentResolver
        val multipartFiles = thumbnailuri?.mapNotNull { uri ->


            val fileName = contentResolver.getFileName(uri)
            val file = File(context.cacheDir, fileName)
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
                file
                MultipartBody.Part.createFormData(
                    "lesson_thumbnail",
                    file.name,
                    file.asRequestBody()
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
        return try {
            val response = api.createCourseResource(
                courseId = courseId,

                thumbnailuri = multipartFiles



                )
            if(response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.oops_something_went_wrong))
            }
        }  catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }




    }






    override suspend fun AddToBookmarkedCourses(userId: String, courseId: String): SimpleResource {
        return try {
            val response = api.createBookmark(userId, courseId)
            if (response.successful) {
                Resource.Success(response.data)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getPopularCategories(page: Int, pageSize: Int): Resource<List<CateforyOverView>> {
        return try {
            val categories = api.getPopularCategories(
                page = page,
                pageSize = pageSize
            )
            println("Categories: $categories")
            Resource.Success(data = categories)

        } catch (e: IOException) {
            println("Error: ${e.message}")
            println("Error: ${e.cause}")
            e.printStackTrace()
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_serverca)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getMostWatchedCourses(
        page: Int,
        pageSize: Int
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getMostWatchedCourses(
                page = page,
                pageSize = pageSize
            )
            Resource.Success(data = courses)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getPreviousWatchedCourses(
        studentId: String,
        page: Int,
        pageSize: Int
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getPreviousWatchedCourses(
                studentId,
                page = page,
                pageSize = pageSize
            )
            Resource.Success(data = courses)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getOthersWatchedCourses(
        page: Int,
        pageSize: Int
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getOthersWatchedCourses(
                page = page,
                pageSize = pageSize
            )
            Resource.Success(data = courses)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getDiscoverCourses(
        filterResult: FilterResult?
    ): Flow<PagingData<CourseOverview>> = flow {
        remote.getDiscoverCourses(filterResult).collect { emit(it) }
    }


    override suspend fun getCourseDetails(courseId: String): Resource<Course?> {
        return try {
            val response = api.getCourseDetails(courseId = courseId)

            if (response.successful) {
                Resource.Success(response.data)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getCommentsForCourse(courseId: String): Resource<List<Comment>> {
        return try {
            val comments = api.getCommentsForCourse(courseId = courseId).data?.map { commentDto ->
                commentDto.toComment()
            }
            Resource.Success(comments)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getLessonsForCourse(courseId: String): Resource<List<Lesson>> {
        return try {
            val lessons = api.getLessonsForCourse(courseId = courseId).data
            Resource.Success(lessons)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getProjectsForCourse(courseId: String): Resource<List<Project>> {
        return try {
            val projects = api.getProjectsForCourse(courseId = courseId).data
            Resource.Success(projects)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getCoursesforTeacherPaged(
        page: Int,
        pageSize: Int,
        Teacherid: String
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getCoursesforTeacherPaged(
                Teacherid = Teacherid,
                page = page,
                pageSize = pageSize
            ).data
            Resource.Success(data = courses)
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

    override suspend fun getHighlyRatedCrs(
        page: Int,
        pageSize: Int,
        Teacherid: String
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getRatedCrs(
                Teacherid = Teacherid,
                page = page,
                pageSize = pageSize
            ).data
            Resource.Success(data = courses)
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

    override suspend fun getHighlyEnrolledCrs(
        page: Int,
        pageSize: Int,
        Teacherid: String
    ): Resource<List<CourseOverview>> {
        return try {
            val courses = api.getEnrolledCrs(
                Teacherid = Teacherid,
                page = page,
                pageSize = pageSize
            ).data
            Resource.Success(data = courses)
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


    override suspend fun getResourcesForCourse(courseId: String): Resource<List<CourseResource>> {
        return try {
            val resources = api.getResourcesForCourse(courseId = courseId).data
            Resource.Success(resources)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getResourcesForCourseLesson(

        lessonId: String
    ): Resource<List<CourseResource>> {
        return try {
            val resources = api.getResourcesForCourseLesson(lessonId=lessonId).data
            Resource.Success(resources)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }

    }

    override suspend fun createComment(courseId: String, comment: String): SimpleResource {
        return try {
            val response = api.createComment(
                CreateCommentRequest(
                    comment = comment,
                    courseId = courseId,
                )
            )
            if (response.successful) {
                Resource.Success(response.data)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun createLesson(
        courseId: String,
        createLessonRequest: CreateLessonRequest,
        thumbnailuri: Uri?,
        videouri: Uri?,
        context: Context
    ): SimpleResource {
        val contentResolver = context.contentResolver
        val thumbnailFile: File? = thumbnailuri?.let {
            val fileName = contentResolver.getFileName(it)
            val file = File(context.cacheDir, fileName)
            try {
                val inputStream = contentResolver.openInputStream(it)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        val introVideoFile: File? = videouri?.let {
            val fileName = contentResolver.getFileName(it)
            val file = File(context.cacheDir, fileName)
            try {
                val inputStream = contentResolver.openInputStream(it)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
        return try {
            val response = api.createLessonForCourse(
                courseId = courseId,
                CreateLessonRequest = MultipartBody.Part
                    .createFormData(
                        "lesson_data",
                        Json.encodeToString(createLessonRequest)

                    ),
                thumbnailuri = thumbnailFile?.let {
                    MultipartBody.Part
                        .createFormData(
                            "lesson_thumbnail",
                            thumbnailFile.name,
                            thumbnailFile.asRequestBody()
                        )
                },
                introvideouri = introVideoFile?.let {
                    MultipartBody.Part
                        .createFormData(
                            "lesson_videoUrl",
                            introVideoFile.name,
                            introVideoFile.asRequestBody()
                        )
                },

                )
            if(response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        }  catch(e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }



    }

    override suspend fun likeParent(parentId: String, parentType: Int,userId: String): SimpleResource {
        return try {
            val response = api.likeParent(
                LikeUpdateRequest(
                    parentId = parentId,
                    parentType = parentType,
                    userid = userId
                )
            )
            if (response.successful) {
                Resource.Success(response.data)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun unlikeParent(parentId: String, parentType: Int): SimpleResource {
        return try {
            val response = api.unlikeParent(
                parentId = parentId,
                parentType = parentType,

            )
            if (response.successful) {
                Resource.Success(response.data)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getLikesForParent(parentId: String): Resource<List<UserItem>> {
        return try {
            val response = api.getLikesForParent(
                parentId = parentId,
            )
            Resource.Success(response.map { it.toUserItem() })
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getLessonDetails(lessonId: String): Resource<Lesson?> {
        return try {
            val resources = api.getLessonDetails(lessonId= lessonId).data
            Resource.Success(resources)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }

    }

    override suspend fun deleteCourse(courseId: String): SimpleResource {
        return try {
        val response = api.deleteCourse(
           courseId=courseId

            )
        if (response.successful) {
            Resource.Success(response.data)
        } else {
            response.message?.let { msg ->
                Resource.Error(UiText.DynamicString(msg))
            } ?: Resource.Error(UiText.StringResource(R.string.error_unknown))
        }
    } catch (e: IOException) {
        Resource.Error(
            uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
        )
    } catch (e: HttpException) {
        Resource.Error(
            uiText = UiText.StringResource(R.string.oops_something_went_wrong)
        )
    }

    }
}