package dev.sanskar.featuretesteduco.feature_course.presentation.category_list


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import dev.sanskar.featuretesteduco.core.presentation.components.CategoryCard
import dev.sanskar.featuretesteduco.feature_course.presentation.home.HomeViewModel
import dev.sanskar.featuretesteduco.ui.theme.Grey900
import dev.sanskar.featuretesteduco.ui.theme.title2new
import dev.sanskar.featuretesteduco.R


@Composable
fun PopularCategoryListScreen(
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit,
    imageLoader: ImageLoader,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val categories = viewModel.categoryPagingState.value.items

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onNavigateUp() },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_hearder), contentDescription = null)
            }
            Text(text = "All Popular Categories", color = Grey900, style = title2new)

        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(horizontal = 20.dp),
            columns = StaggeredGridCells.Fixed(2),


            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            categories.let {
                items(categories) { category ->
                    CategoryCard(category = category, imageLoader = imageLoader)
                }
            }

        }
    }

}