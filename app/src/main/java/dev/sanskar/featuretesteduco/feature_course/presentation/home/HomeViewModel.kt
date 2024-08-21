package dev.sanskar.featuretesteduco.feature_course.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.domain.models.CateforyOverView
import dev.sanskar.featuretesteduco.core.domain.models.Category
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.presentation.PagingState
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.DefaultPaginator
import dev.sanskar.featuretesteduco.core.util.Event
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetMostWatchedCourseUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetOthersWatchedCourses
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetPopularCategoriesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetPreviousWatchedCoursesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.SearchCoursesUseCase
import dev.sanskar.featuretesteduco.feature_profile.domain.model.ProfileHeader
import dev.sanskar.featuretesteduco.feature_profile.domain.use_case.GetProfileHeaderUseCase
import dev.sanskar.featuretesteduco.feature_profile.presentation.profile.ProfileHeaderState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProfileHeader: GetProfileHeaderUseCase,
    private val getOwnUserId: GetOwnUserIdUseCase,
    private val getCategories: GetPopularCategoriesUseCase,
    private val getMostWatchedCourseUseCase: GetMostWatchedCourseUseCase,
    private val getPreviousWatchedCoursesUseCase: GetPreviousWatchedCoursesUseCase,
    private val searchCourses: SearchCoursesUseCase,
    private val getOthersWatchedCourses: GetOthersWatchedCourses
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _profileHeaderState = mutableStateOf(ProfileHeaderState(ProfileHeader(getOwnUserId.toString())))
    val profileHeaderState: State<ProfileHeaderState> = _profileHeaderState

//    private val _categoryPagingStat = mutableStateOf(Category2())
//    val categoryPagingStat: State<Category2> = _categoryPagingStat



    private val _categoryPagingState = mutableStateOf<PagingState<CateforyOverView>>(PagingState())
    val categoryPagingState: State<PagingState<CateforyOverView>> = _categoryPagingState

    private val _mostWatchedCoursePagingState = mutableStateOf<PagingState<CourseOverview>>(PagingState())
    val mostWatchedCoursePagingState: State<PagingState<CourseOverview>> = _mostWatchedCoursePagingState

    private val _previousWatchedCoursePagingState = mutableStateOf<PagingState<CourseOverview>>(PagingState())
    val previousWatchedCoursePagingState: State<PagingState<CourseOverview>> = _previousWatchedCoursePagingState

    private val _othersWatchedCoursePagingState = mutableStateOf<PagingState<CourseOverview>>(PagingState())
    val othersWatchedCoursePagingState: State<PagingState<CourseOverview>> = _othersWatchedCoursePagingState

    private val _searchQuery = mutableStateOf(StandardTextFieldState())
    val searchQuery: State<StandardTextFieldState> = _searchQuery
    private val _searchCoursesPagingState = mutableStateOf<PagingState<CourseOverview>>(PagingState())
    val searchCoursesPagingState: State<PagingState<CourseOverview>> = _searchCoursesPagingState


    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.EnteredSearchQuery -> {
                _searchQuery.value = searchQuery.value.copy(
                    text = event.query
                )
            }
        }

    }

    private val searchCoursesPaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _searchCoursesPagingState.value = searchCoursesPagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            searchCourses(page = page, query = searchQuery.value.text)
        },
        onSuccess = { courses ->
            _searchCoursesPagingState.value = searchCoursesPagingState.value.copy(
                items = searchCoursesPagingState.value.items + courses,
                endReached = courses.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )

    private val categoryPaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _categoryPagingState.value = categoryPagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getCategories(page = page)



        },

        onSuccess = { posts ->
            _categoryPagingState.value = categoryPagingState.value.copy(
                items = categoryPagingState.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )

    private val mostWatchedCoursePaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _mostWatchedCoursePagingState.value = mostWatchedCoursePagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getMostWatchedCourseUseCase(page = page)
        },
        onSuccess = { posts ->
            _mostWatchedCoursePagingState.value = mostWatchedCoursePagingState.value.copy(
                items = mostWatchedCoursePagingState.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )
    private val previousWatchedCoursePaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _previousWatchedCoursePagingState.value = previousWatchedCoursePagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getPreviousWatchedCoursesUseCase(studentId = getOwnUserId(), page = page)
        },
        onSuccess = { posts ->
            println("yjc tyidcxtrjx fgjj XRJYCIYKCTHXTRJHCG IYDHGCGYKCXGKG IYTDCYKICYKC ${posts}")
            _previousWatchedCoursePagingState.value = previousWatchedCoursePagingState.value.copy(
                items = previousWatchedCoursePagingState.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )
    private val othersWatchedCoursePaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _othersWatchedCoursePagingState.value = othersWatchedCoursePagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            getOthersWatchedCourses( page = page)
        },
        onSuccess = { posts ->
            _othersWatchedCoursePagingState.value = othersWatchedCoursePagingState.value.copy(
                items = othersWatchedCoursePagingState.value.items + posts,
                endReached = posts.isEmpty(),
                isLoading = false
            )
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
        }
    )


    init {
        println("DFhbd")
        getProfileHeader()
        println("DFhbd")
        loadNextCategories()
        println("DFhbd")
        loadNextMostWatchedCourses()
        loadNextSearchCourses()
        loadNextPreviousWatchedCourses()
        loadNextOthersWatchedCourses()
    }

    fun loadNextSearchCourses() {
        viewModelScope.launch {
            searchCoursesPaginator.loadNextItems()
        }
    }


     fun loadNextMostWatchedCourses() {
        viewModelScope.launch {
            mostWatchedCoursePaginator.loadNextItems()
        }
    }
    fun loadNextPreviousWatchedCourses() {
        viewModelScope.launch {
            previousWatchedCoursePaginator.loadNextItems()
        }
    }
    fun loadNextOthersWatchedCourses() {
        viewModelScope.launch {
            othersWatchedCoursePaginator.loadNextItems()
        }
    }

    fun loadNextCategories() {
        println("called loadcs")
          viewModelScope.launch {
              categoryPaginator.loadNextItems()
          }
//              _categoryPagingStat.value=categoryPagingStat.value.copy(
//                  isLoading = true
//              )
//              val result=getCategories()
//              println("yhi se asya h ye mc")
//              println(result)
//              when (result) {
//                  is Resource.Success -> {
//                      _categoryPagingStat.value= result.data?.let {
//                          categoryPagingStat.value.copy(
//                              items = categoryPagingStat.value.items + it,
//                              isLoading = false
//                          )
//                      }!!
//
//              }
//
//                  is Resource.Error ->{
//                      _categoryPagingStat.value=categoryPagingStat.value.copy(
//                          isLoading = false
//                      )
//                      _eventFlow.emit(
//                          UiEvent.ShowSnackBar(
//                              uiText = result.uiText ?: UiText.unknownError()
//                          ))
//
//                  }
//
//
//                  }                                }
//
//        viewModelScope.launch {
//            _categoryPagingStat.value = categoryPagingStat.value.copy(
//                isLoading = true
//            )
//            val result=getCategories()
//            println("yhi se asya h ye mc")
//            println(result)
//            when (result) {
//                is Resource.Success -> {
//                    _categoryPagingStat.value= result.data?.let {
//                        categoryPagingStat.value.copy(
//                            items = categoryPagingStat.value.items + it,
//                            isLoading = false
//                        )
//
//
//                    }!!
//                }

//                is Resource.Error ->{
//                    _categoryPagingStat.value=categoryPagingStat.value.copy(
//                        isLoading = false
//                    )
//                    _eventFlow.emit(
//                        UiEvent.ShowSnackBar(
//                            uiText = result.uiText ?: UiText.unknownError()
//                    ))
//
//                }
            }



    private fun getProfileHeader() {
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
}
