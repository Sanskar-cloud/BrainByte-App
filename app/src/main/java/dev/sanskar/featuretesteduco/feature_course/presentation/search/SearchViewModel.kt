package dev.sanskar.featuretesteduco.feature_course.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.core.domain.models.Course
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetDiscoverCoursesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.GetPopularCategoriesUseCase
import dev.sanskar.featuretesteduco.feature_course.domain.use_case.SearchCoursesUseCase
import dev.sanskar.featuretesteduco.feature_course.presentation.search.use_cases.SearchCourseUseCase2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
   private val searchCourseUseCase2: SearchCourseUseCase2
): ViewModel() {

    private val _searchQuery =  mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    private val _isBookmarked = mutableStateOf(false)
    val isBookmarked: State<Boolean> = _isBookmarked

    private val _searchResults = MutableStateFlow<List<Course>>(emptyList())
    val searchResults: StateFlow<List<Course>> = _searchResults.asStateFlow()

    fun onSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            val results = searchCourseUseCase2(query)
            if (results != null) {
                _searchResults.value = results
            }
        }
    }


}