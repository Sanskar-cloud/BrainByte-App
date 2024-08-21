package dev.sanskar.featuretesteduco.feature_course.presentation.util

import dev.sanskar.featuretesteduco.core.util.Error
sealed class CommentError : Error() {
    object FieldEmpty: CommentError()
}