package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.Event
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateLessonRequest
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateCourseResourceUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateLessonUseCase
import dev.sanskar.featuretesteduco.feature_settings.presentation.CreateCourseResourceState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class LessonViewmodel @Inject constructor(
    private val repository: CourseRepository,
    private val createLessonUseCase: CreateLessonUseCase,
    private val createCourseResourceUseCase: CreateCourseResourceUseCase


): ViewModel()  {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _lessonnoState = mutableStateOf(StandardTextFieldState())
    val lessonnoState: State<StandardTextFieldState> = _lessonnoState
    private val _nameState = mutableStateOf(StandardTextFieldState())
    val nameState: State<StandardTextFieldState> = _nameState
    private val _descState = mutableStateOf(StandardTextFieldState())
    val descState: State<StandardTextFieldState> = _descState
    private val _createLesson = mutableStateOf(CreateLessonState())
    val createLesson: State<CreateLessonState> = _createLesson
    private val _createCourseResource= mutableStateOf(CreateCourseResourceState())
    val createCourseResource: State<CreateCourseResourceState> = _createCourseResource

    val selectedResourceUri = mutableStateOf<List<Uri>?>(null)


    val selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedVideoUri = MutableLiveData<Uri?>(null)

    fun onEvent(event: CreateLessonEvent) {
        when (event) {


            is CreateLessonEvent.EnteredLessonNo-> {
                _lessonnoState.value = lessonnoState.value.copy(
                    text = event.lessonNo.toString()
                )
            }

            is CreateLessonEvent.EnteredLessonName -> {
                _nameState.value = nameState.value.copy(
                    text = event.name
                )
            }

            is CreateLessonEvent.EnteredDescription -> {
                _descState.value = descState.value.copy(
                    text = event.desc
                )
            }




            is CreateLessonEvent.SaveLesson -> {
                viewModelScope.launch {
                    _createLesson.value= createLesson.value.copy(isLoading = true)
                    _createLesson.value = createLesson.value.copy(
                        createLessonRequest = CreateLessonRequest(
                            lessonNo = lessonnoState.value.text,
                            name = nameState.value.text,
                            desc = descState.value.text


                        )
                    )

                    val result = createLesson.value.createLessonRequest?.let {

                        createLessonUseCase(
                            courseId = event.courseId,
                            createLessonRequest = it,
                            thumbnailuri = selectedImageUri.value,
                            videouri = selectedVideoUri.value,
                            context = event.context


                            )
                    }
                    _createLesson.value = createLesson.value.copy(isLoading = false)

                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(


                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.StringResource(R.string.lesson_created)
                                ),


                            )
                            _eventFlow.emit(UiEvent.Navigate(Screen.TeacherHomeScreen.route))
                        }

                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.unknownError()
                                )
                            )
                        }

                        else -> {}
                    }
                }
            }

            CreateLessonEvent.Cancel -> TODO()
            is CreateLessonEvent.DeleteCourse -> TODO()
            is CreateLessonEvent.UploadResource ->{
                viewModelScope.launch {
                    _createCourseResource.value= createCourseResource.value.copy(isLoading = true)
                    val result=createCourseResource.value?.let {
                        createCourseResourceUseCase(
                            courseId = event.courseId,
                            thumbnailuri = selectedResourceUri.value,
                            context = event.context
                        )
                    }
                    _createCourseResource.value= createCourseResource.value.copy(isLoading = false)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.StringResource(R.string.resource_created)
                                )
                            )
                            _eventFlow.emit(UiEvent.Navigate(Screen.TeacherHomeScreen.route))
                        }

                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.unknownError()
                                )
                            )
                        }


                        else -> {}
                }
            }
        }

            }
        }}







