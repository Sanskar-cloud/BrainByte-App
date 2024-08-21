package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.models.PaymentStatus
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.ParentType
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_auth.domain.use_case.AuthenticateUseCase
import dev.sanskar.featuretesteduco.feature_course.data.remote.PaymentApi
import dev.sanskar.featuretesteduco.feature_course.data.remote.PaymentRequestStudent
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.*
import dev.sanskar.featuretesteduco.feature_course.presentation.util.CommentError
import dev.sanskar.featuretesteduco.feature_profile.domain.use_case.ProfileUseCases
import dev.sanskar.featuretesteduco.feature_profile.presentation.profile.ProfileState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val authenticate: AuthenticateUseCase,
    private val getOwnUserId: GetOwnUserIdUseCase,
    private val getCourseDetails: GetCourseDetailsUseCase,
    private val getCommentsForCourse: GetCommentsForCourseUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val getLessonsForCourse: GetLessonsForCourse,
    private val sharedPref: SharedPreferences,
    private val getResourcesForCourse: GetResourcesForCourse,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val addToBookmarkedCourses: AddToBookmarkedCoursesUseCase,
    private val profileUseCases: ProfileUseCases

) : ViewModel() {
    private val apiService = PaymentApi.create()

    private val _state = MutableStateFlow(CourseDetailState())
    val state: StateFlow<CourseDetailState> = _state.asStateFlow()
    private val _userProfile = MutableStateFlow(ProfileState())
    val userProfile: StateFlow<ProfileState> = _userProfile
    private val _courseOverviewState = MutableStateFlow(CourseOverviewState())
    val courseOverviewState: StateFlow<CourseOverviewState> = _courseOverviewState.asStateFlow()



    private val _isBookmarked = mutableStateOf(false)
    val isBookmarked: State<Boolean> = _isBookmarked

    /*private val _lessonsState = MutableStateFlow(LessonDetailState())
    val lessonsState: StateFlow<LessonDetailState> = _lessonsState.asStateFlow()

    private val _commentsState = MutableStateFlow(CommentDetailState())
    val commentsState: StateFlow<CommentDetailState> = _commentsState.asStateFlow()

    private val _projectsState = MutableStateFlow(ProjectDetailState())
    val projectsState: StateFlow<ProjectDetailState> = _projectsState.asStateFlow()

    private val _resourcesState = MutableStateFlow(ResourceDetailState())
    val resourcesState: StateFlow<ResourceDetailState> = _resourcesState.asStateFlow()
*/

    private val _userState = mutableStateOf(UserDetailState())
    val userState: State<UserDetailState> = _userState

    private val _commentTextFieldState = mutableStateOf(StandardTextFieldState(error = CommentError.FieldEmpty))
    val commentTextFieldState: State<StandardTextFieldState> = _commentTextFieldState

    private val _commentState = mutableStateOf(CommentState())
    val commentState: State<CommentState> = _commentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var isUserLoggedIn = false

    init {

        savedStateHandle.get<String>("courseId")?.let { courseId ->
            loadCourseDetails(courseId)
            loadCommentsForCourse(courseId)
            loadLessonsForCourse(courseId)
            loadResourcesForCourse(courseId)
        }
        getProfile()
    }

    fun onEvent(event: CourseDetailEvent) {
        when(event) {
            is CourseDetailEvent.Comment -> {
                createComment(
                    courseId = savedStateHandle.get<String>("courseId") ?: "",
                    comment = commentTextFieldState.value.text
                )
            }
            is CourseDetailEvent.LikeComment -> {
                val isLiked = state.value.comments.find {
                    it.id == event.commentId
                }?.isLiked == true
                toggleLikeForParent(
                    parentId = event.commentId,
                    parentType = ParentType.Comment.type,
                    isLiked = isLiked,
                    userId = getOwnUserId()
                )
            }
            is CourseDetailEvent.EnteredComment -> {
                _commentTextFieldState.value = commentTextFieldState.value.copy(
                    text = event.comment,
                    error = if(event.comment.isBlank()) CommentError.FieldEmpty else null
                )
            }
        }
    }

    fun addCourseToBookmark() {
        viewModelScope.launch {
            val courseId = savedStateHandle.get<String>("courseId")?: ""
            val result = addToBookmarkedCourses(
                userId = getOwnUserId(),
                courseId = courseId
            )

            when (result) {
                is Resource.Success -> {
                    _isBookmarked.value = true
                    _eventFlow.emit(UiEvent.ShowSnackBar(
                        uiText = UiText.StringResource(R.string.bookmarked_course)
                    ))


                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackBar(
                        uiText = result.uiText ?: UiText.unknownError()
                    ))
                }
            }
        }
    }
    fun getProfile() {
        viewModelScope.launch {
            _userProfile.value = userProfile.value.copy(
                isLoadingProfile = true
            )
            val userId = sharedPref.getString(Constants.KEY_USER_ID, "")
            val result = userId?.let { profileUseCases.getProfile(it) }
            println("vkfy jmhgdd ydchjfcvhuy fhv"+result)
            when (result) {
                is Resource.Success -> {
                    _userProfile.value = userProfile.value.copy(
                        profile = result.data,
                        isLoadingProfile = false
                    )
                }
                is Resource.Error -> {
                    _userProfile.value = userProfile.value.copy(
                        isLoadingProfile = false
                    )
                    // Handle error
                }

                null -> TODO()
            }
        }
    }

    private fun toggleLikeForParent(
        parentId: String,
        parentType: Int,
        isLiked: Boolean,
        userId:String,
    ) {
        viewModelScope.launch {
            isUserLoggedIn = authenticate() is Resource.Success
            if(!isUserLoggedIn) {
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_not_logged_in)))
                return@launch
            }
            when(parentType) {
                ParentType.Comment.type -> {
                    _state.value = state.value.copy(
                        comments = state.value.comments.map { comment ->
                            if(comment.id == parentId) {
                                comment.copy(
                                    isLiked = !isLiked,
                                    likeCount = if (isLiked) {
                                        comment.likeCount - 1
                                    } else comment.likeCount + 1
                                )
                            } else comment
                        }
                    )
                }
            }
            val result = toggleLikeForParentUseCase(
                parentId = parentId,
                parentType = parentType,
                isLiked = isLiked,
                userId = userId
            )
            when(result) {
                is Resource.Success -> Unit
                is Resource.Error -> {
                    when(parentType) {
                        ParentType.Comment.type -> {
                            _state.value = state.value.copy(
                                comments = state.value.comments.map { comment ->
                                    if(comment.id == parentId) {
                                        comment.copy(
                                            isLiked = isLiked,
                                            likeCount = if(comment.isLiked) {
                                                comment.likeCount - 1
                                            } else comment.likeCount + 1
                                        )
                                    } else comment
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createComment(courseId: String, comment: String) {
        viewModelScope.launch {
            isUserLoggedIn = authenticate() is Resource.Success
            if(!isUserLoggedIn) {
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_not_logged_in)))
                return@launch
            }

            _commentState.value = commentState.value.copy(
                isLoading = true
            )
            val result = createCommentUseCase(
                courseId = courseId,
                comment = comment
            )
            when(result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvent.ShowSnackBar(
                        uiText = UiText.StringResource(R.string.comment_posted)
                    ))
                    _commentState.value = commentState.value.copy(
                        isLoading = false
                    )
                    _commentTextFieldState.value = commentTextFieldState.value.copy(
                        text = "",
                        error = CommentError.FieldEmpty
                    )
                    loadCommentsForCourse(courseId)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackBar(
                        uiText = result.uiText ?: UiText.unknownError()
                    ))
                    _commentState.value = commentState.value.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadCourseDetails(courseId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoadingCourse = true
            )
            _courseOverviewState.value = courseOverviewState.value.copy(
                isLoadingOverview = true
            )
            val result = getCourseDetails(courseId)
            println(result.data)
            when(result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        course = result.data,

                        isLoadingCourse = false
                    )
                    _courseOverviewState.value = courseOverviewState.value.copy(
                        rating = result.data?.avgRating,
                        description = result.data?.description,
                        noOfStudentEnrolled = result.data?.noOfStudentEnrolled,
                        commentCount = result.data?.commentCount,

//                        courseIntroVideoUrl = result.data?.courseIntroVideoUrl,
                        isLoadingOverview = false

                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoadingCourse = false
                    )
                    _courseOverviewState.value = courseOverviewState.value.copy(
                        isLoadingOverview = false
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

    private fun loadCommentsForCourse(courseId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoadingComments = true
            )
            val result = getCommentsForCourse(courseId)
            when(result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        comments = result.data ?: emptyList(),
                        isLoadingComments = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoadingComments = false
                    )
                }
            }
        }
    }



    private fun loadResourcesForCourse(courseId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoadingResources = true
            )
            val result = getResourcesForCourse(courseId)
            when(result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        resources = result.data ?: emptyList(),
                        isLoadingResources = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoadingResources = false
                    )
                }
            }
        }
    }

    private fun loadLessonsForCourse(courseId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoadingLessons = true
            )
            val result = getLessonsForCourse(courseId)
            when(result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        lessons = result.data ?: emptyList(),
                        isLoadingLessons = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoadingLessons = false
                    )
                }
            }
        }
    }
    fun initiatePaymentForStudent(StudentId: String, StudentName: String, StudentEmail: String, courseId: String ,onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.createPaymentForStudent(PaymentRequestStudent(StudentId, StudentName,StudentEmail,courseId))

                onResult(response.paymentLinkUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult("")
            }
        }
    }
    fun hasStudentPaid(courseId: String, userId: String, onResult: (Boolean) -> Unit) {
        // Implement the logic to call your backend and check if the payment is completed
        viewModelScope.launch {
            // Assuming `courseService` is an injected service that handles the backend call
            val isPaid = apiService.findStudentByPayment(courseId, userId, PaymentStatus.COMPLETED)
            onResult(isPaid)
        }
    }

}

