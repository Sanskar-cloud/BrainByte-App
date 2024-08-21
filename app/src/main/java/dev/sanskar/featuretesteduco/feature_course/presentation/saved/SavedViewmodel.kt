package dev.sanskar.featuretesteduco.feature_course.presentation.saved

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.presentation.PagingState
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.DefaultPaginator
import dev.sanskar.featuretesteduco.core.util.Event
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.AddToBookmarkedCoursesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetBookmarkedCoursesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val addToBookmarkedCoursesUseCase: GetBookmarkedCoursesUseCase,
    private val getOwnUserId: GetOwnUserIdUseCase
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _bookmarkedCourses = MutableStateFlow(BookmarkState())
    val bookmarkedCourses: StateFlow<BookmarkState> = _bookmarkedCourses.asStateFlow()

    private val _CoursePagingState = mutableStateOf<PagingState<CourseOverview>>(
        PagingState()
    )
    val CoursePagingState: State<PagingState<CourseOverview>> = _CoursePagingState



    private val CoursePaginator = DefaultPaginator(
        onLoadUpdated = { isLoading ->
            _CoursePagingState.value = CoursePagingState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { page ->
            addToBookmarkedCoursesUseCase(page = page, userId =getOwnUserId())
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

    init {

        loadCoursesForTchr()
    }



    fun loadCoursesForTchr() {
        viewModelScope.launch {
            CoursePaginator.loadNextItems()
        }
    }


//    fun addCourseToBookmarks(userId: String) {
//        viewModelScope.launch {
//            _bookmarkedCourses.value = _bookmarkedCourses.value.copy(isLoading = true)
//            when (val result = addToBookmarkedCoursesUseCase(userId)) {
//                is Resource.Success -> {
//                    // Assuming the use case returns a new list of bookmarked courses
//                    _bookmarkedCourses.value = result.data?.let {
//                        _bookmarkedCourses.value.copy(
//                            courses = it,
//                            isLoading = false
//                        )
//                    }!!
//                }
//                is Resource.Error -> {
//                    _bookmarkedCourses.value = _bookmarkedCourses.value.copy(isLoading = false)
//                }
//            }
//        }
//    }
}
