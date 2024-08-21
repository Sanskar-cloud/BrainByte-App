package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetLessonDetailsUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetResourcesForCourseLessonUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val getLessonDetailsUseCase: GetLessonDetailsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getResourcesForCourseLessonUseCase: GetResourcesForCourseLessonUseCase,

    ): ViewModel() {
    private val _lessonsState = MutableStateFlow(LessonDetailState())
    val lessonsState: StateFlow<LessonDetailState> = _lessonsState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _resourcesState = MutableStateFlow(ResourcesState())
    val resourcesState: StateFlow<ResourcesState> = _resourcesState


    init {

        savedStateHandle.get<String>("_id")?.let { id ->
            loadLessonDetails(id)
            loadResources(id)

        }
    }

    fun loadLessonDetails(lessonId: String) {
        viewModelScope.launch {
            _lessonsState.value = lessonsState.value.copy(
                isLoadingLesson = true
            )

            val result = getLessonDetailsUseCase(lessonId)
            println(result.data)
            when (result) {
                is Resource.Success -> {
                    _lessonsState.value = lessonsState.value.copy(
                        lesson = result.data,

                        isLoadingLesson = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )

                }

                is Resource.Error -> {
                    _lessonsState.value = lessonsState.value.copy(
                        isLoadingLesson = false
                    )

                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }

                is Resource.Error -> TODO()
                is Resource.Success -> TODO()
                is Resource.Error -> TODO()
                is Resource.Success -> TODO()
            }
        }
    }

    fun loadResources( lessonId: String) {
        viewModelScope.launch {
            _resourcesState.value = resourcesState.value.copy(isLoading = true)
            val result = getResourcesForCourseLessonUseCase( lessonId)
            when (result) {
                is Resource.Success -> {
                    _resourcesState.value = result.data?.let {
                        resourcesState.value.copy(
                            resources = it,

                            isLoading = false
                        )
                    }!!
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )

                }

                is Resource.Error -> {
                    _resourcesState.value = resourcesState.value.copy(
                        isLoading = false
                    )

                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }

            }
        }
    }
}