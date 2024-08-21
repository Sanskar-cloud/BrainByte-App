package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.presentation.PagingState
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.DefaultPaginator

import dev.sanskar.featuretesteduco.core.util.Event
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.data.remote.ApiResponse
import dev.sanskar.featuretesteduco.feature_course.data.remote.PaymentApi
import dev.sanskar.featuretesteduco.feature_course.data.remote.PaymentRequest
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateCourseResourceUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.CreateCourseUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.DeleteCourseUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetCoursesForTeacherUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetHighlyEnrolledCrs
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetHighlyRatedCrs
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetMostWatchedCourseUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetPopularCategoriesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.SearchCoursesUseCase
import dev.sanskar.featuretesteduco.feature_profile.domain.model.ProfileHeader
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData
import dev.sanskar.featuretesteduco.feature_profile.domain.use_case.GetCoursesForProfileUseCase
import dev.sanskar.featuretesteduco.feature_profile.domain.use_case.GetProfileHeaderUseCase
import dev.sanskar.featuretesteduco.feature_profile.domain.use_case.ProfileUseCases
import dev.sanskar.featuretesteduco.feature_profile.presentation.profile.ProfileHeaderState
import dev.sanskar.featuretesteduco.feature_profile.presentation.profile.ProfileState
import dev.sanskar.featuretesteduco.feature_settings.presentation.CreateCourseState
import dev.sanskar.featuretesteduco.feature_settings.presentation.ProfileSettingsState
import dev.sanskar.featuretesteduco.feature_settings.presentation.SettingsEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val getProfileHeader: GetProfileHeaderUseCase,

    private val getOwnUserId: GetOwnUserIdUseCase,
    private val sharedPref: SharedPreferences,
    private val getCoursesForTeacherUseCase: GetCoursesForTeacherUseCase,
    private val createCourseRequestUseCase: CreateCourseUseCase,
    private val profileUseCases: ProfileUseCases,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val createCourseResourceUseCase: CreateCourseResourceUseCase,
    private val getHighlyEnrolledCrs: GetHighlyEnrolledCrs,
    private val getHighlyRatedCrs: GetHighlyRatedCrs





    ): ViewModel() {
        private val apiService = PaymentApi.create()
    private val _userProfile = MutableStateFlow(ProfileState())
    val userProfile: StateFlow<ProfileState> = _userProfile

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _titleStae = mutableStateOf(StandardTextFieldState())
    val titleState: State<StandardTextFieldState> = _titleStae
    private val _descriptionState = mutableStateOf(StandardTextFieldState())
    val descriptionState: State<StandardTextFieldState> = _descriptionState
    private val _priceState = mutableStateOf(StandardTextFieldState())
    val priceState: State<StandardTextFieldState> = _priceState
    private val _tagsState = mutableStateOf(StandardTextFieldState())
    val tagsState: State<StandardTextFieldState> = _tagsState
    private val _durationState = mutableStateOf(StandardTextFieldState())
    val durationState: State<StandardTextFieldState> = _durationState

    private val _deleteCourse = mutableStateOf(DeleteCourseState())
    val deleteCourse: State<DeleteCourseState> = _deleteCourse

    private val _createCourse = mutableStateOf(CreateCourseState())
    val createCourse: State<CreateCourseState> = _createCourse


    val selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedVideoUri = MutableLiveData<Uri?>(null)


    private val _tchrCourse = MutableStateFlow(HomeState())
    val tchrCourse: StateFlow<HomeState> = _tchrCourse

    private val _profileHeaderState =
        mutableStateOf(ProfileHeaderState(ProfileHeader(getOwnUserId.toString())))
    val profileHeaderState: State<ProfileHeaderState> = _profileHeaderState

    private val _CoursePagingState = mutableStateOf<PagingState<CourseOverview>>(
        PagingState()
    )
    val CoursePagingState: State<PagingState<CourseOverview>> = _CoursePagingState
    private val _CoursePagingStateRted = mutableStateOf<PagingState<CourseOverview>>(
        PagingState()
    )
    val CoursePagingStateRted: State<PagingState<CourseOverview>> = _CoursePagingStateRted
    private val _CoursePagingStatePop = mutableStateOf<PagingState<CourseOverview>>(
        PagingState()
    )
    val CoursePagingStatePop: State<PagingState<CourseOverview>> = _CoursePagingStatePop



    fun onEvent(event: CreateCourseEvent) {
        when(event) {


            is CreateCourseEvent.EnteredCourseTitle ->{
                _titleStae.value = titleState.value.copy(
                    text = event.title
                )
            }

            is CreateCourseEvent.EnteredDescription -> {
                _descriptionState.value = descriptionState.value.copy(
                    text = event.description
                )
            }

            is CreateCourseEvent.EnteredTag -> {
                _tagsState.value = tagsState.value.copy(
                    text = event.tag
                )
            }

            is CreateCourseEvent.EnteredDuration -> {
                _durationState.value = durationState.value.copy(
                    text = event.duration
                )
            }
            is CreateCourseEvent.EnteredPrice -> {
                _priceState.value = priceState.value.copy(
                    text = event.price
                )
            }



            is CreateCourseEvent.SaveCourse -> {
                viewModelScope.launch {
                    _createCourse.value = createCourse.value.copy(
                        createCourseData = CreateCourseRequest(
                            courseTitle = titleState.value.text,
                            description = descriptionState.value.text,
                            tag = tagsState.value.text,
                            duration = durationState.value.text,
                            price = priceState.value.text

                        )
                    )

                    val result = createCourse.value.createCourseData?.let {

                        createCourseRequestUseCase(
                            createCourseRequest= it,
                            thumbnailuri = selectedImageUri.value,
                            introvideouri = selectedVideoUri.value,
                            context = event.context,

                        )
                    }

                    when(result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.unknownError()
                                )
                            )
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

            CreateCourseEvent.Cancel -> TODO()
            is CreateCourseEvent.DeleteCourse -> {
                viewModelScope.launch {
                    _deleteCourse.value = deleteCourse.value.copy(
                        isLoading = true,
                        courseId = event.courseId

                    )
                    val result = deleteCourseUseCase(event.courseId)
                    _deleteCourse.value = deleteCourse.value.copy(
                        isLoading = false,
                        courseId = null
                    )
                    when(result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.unknownError()
                                )
                            )
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
    }
    fun OnEvent2(event: DeleteCourseEvent){
        when(event){
            is DeleteCourseEvent.DeleteCourse -> {
                viewModelScope.launch {
                    _deleteCourse.value = deleteCourse.value.copy(
                        isLoading = true,
                        courseId = event.courseId

                    )
                    val result = deleteCourseUseCase(event.courseId)
                    _deleteCourse.value = deleteCourse.value.copy(
                        isLoading = false,
                        courseId = null
                    )
                    when(result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: UiText.unknownError()
                                )
                            )
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
    }



    private val CoursePaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _CoursePagingState.value = CoursePagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getCoursesForTeacherUseCase(page = page, pageSize = 1, Teacherid =getOwnUserId())
        },
        onSuccess = { posts ->
            _CoursePagingState.value = CoursePagingState.value.copy(
                items = CoursePagingState.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )
    private val CoursePaginatorRated = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _CoursePagingStateRted.value = CoursePagingStateRted.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getHighlyRatedCrs(page = page, pageSize = 1, Teacherid =getOwnUserId())
        },
        onSuccess = { posts ->
            _CoursePagingStateRted.value = CoursePagingStateRted.value.copy(
                items = CoursePagingStateRted.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )
    private val CoursePaginatorPop = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _CoursePagingStatePop.value = CoursePagingStatePop.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getHighlyEnrolledCrs(page = page, pageSize = 1, Teacherid =getOwnUserId())
        },
        onSuccess = { posts ->
            _CoursePagingStatePop.value = CoursePagingStatePop.value.copy(
                items = CoursePagingStatePop.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )

    init {
        getProfileHeader()
        loadCoursesForTchr()
        getProfile()
        loadCoursesForTchrRated()
        loadCoursesForTchrEnrolled()


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




    fun loadCoursesForTchr() {
        viewModelScope.launch {
            CoursePaginator.loadNextItems()
        }
    }
    fun loadCoursesForTchrRated() {
        viewModelScope.launch {
            CoursePaginatorRated.loadNextItems()
        }
    }
    fun loadCoursesForTchrEnrolled() {
        viewModelScope.launch {
            CoursePaginatorPop.loadNextItems()
        }
    }

     fun getProfileHeader() {
        viewModelScope.launch {
            _profileHeaderState.value = profileHeaderState.value.copy(
                isLoading = true
            )
            val result = getProfileHeader(getOwnUserId())
            when (result) {
                is Resource.Success -> {
                    _profileHeaderState.value = result.data?.let {
                        profileHeaderState.value.copy(
                            profileHeader = it,
                            isLoading = false
                        )
                    }!!
                }
                is Resource.Error -> {
                    _profileHeaderState.value = profileHeaderState.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
    fun initiatePayment(teacherId: String, teacherName: String, teacherEmail: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.createPayment(PaymentRequest(teacherId, teacherName,teacherEmail))

                onResult(response.paymentLinkUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult("")
            }
        }
    }

    fun handlePaymentCallback(paymentId: String, orderId: String, onResult: (ApiResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.handlePaymentCallback(paymentId, orderId)
                onResult(response)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(ApiResponse("Error handling payment callback", false))
            }
        }
    }











}