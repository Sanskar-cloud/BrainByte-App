package dev.sanskar.featuretesteduco.feature_course.presentation.home

import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateLessonRequest

data class CreateLessonState (
    val createLessonRequest: CreateLessonRequest?=null,

    val isLoading: Boolean = false,
){
}