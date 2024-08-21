package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.domain.models.Lesson
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetLessonsForCourse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseLessonsViewModel @Inject constructor(
    private val repository: CourseRepository,
    private val getLessonsForCourse: GetLessonsForCourse,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _lessonState = MutableStateFlow(LessonState())
    val lessonState: StateFlow<LessonState> = _lessonState



    init {
        savedStateHandle.get<String>("courseId")?.let { courseId ->
            loadLessons(courseId)
        }
    }

    fun loadLessons(courseId: String) {
        viewModelScope.launch {
            _lessonState.value = _lessonState.value.copy(isLoading = true)
            val result = getLessonsForCourse(courseId)
            print(result)
            _lessonState.value = result.data?.let {
                _lessonState.value.copy(
                    lessons = it,
                    isLoading = false
                )
            }!!
        }
    }
}

data class LessonState(
    val lessons: List<Lesson> = emptyList(),
    val isLoading: Boolean = false
)
