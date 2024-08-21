package dev.sanskar.featuretesteduco.core.domain.models

import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.presentation.course_detail.CourseOverviewState
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData
import dev.sanskar.featuretesteduco.util.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Course(
    val _id: String,
    val userId: String? = "",
    val courseTitle: String,
    val courseTeacher: Teacher,
    val price:String?=null,
    val courseThumbnail: String,
    val courseIntroVideoUrl: String,
    val description: String,
    val moreDetails: String? = null,
    val noOfStudentRated: Int,
    val noOfStudentEnrolled: Int,
    val noOfLessons: Int,
    val avgRating: Double,
    val tag: String? = null,
    val commentCount: Int = 0,

    val duration: String? = null
) {

    fun toCourseOverview(): CourseOverviewState {
        return CourseOverviewState(
            rating = avgRating,
            description = description
        )
    }
    fun toCreateCourseRequest():CreateCourseRequest {
        return CreateCourseRequest(
            courseTitle =  courseTitle,
            description = description,
            tag = tag,

            duration = duration


        )
    }
}

@Serializable
data class NewCourse(
    val courseId: String,
    val ownerUserId: String,
    val courseTitle: String,
    val courseThumbnailUrl: String,
    val courseIntroVideoUrl: String,
    val description: String,
    val duration: Long,
    val noOfLessons: Int,
    val noOfStudentRated: Int,
    val noOfStudentEnrolled: Int,
    val avgRating: Double,
    val tag: String? = null,
    val commentCount: Int = 0,
) {

    fun toCourseOverview(): CourseOverviewState {
        return CourseOverviewState(
            rating = avgRating,
            description = description
        )
    }
}