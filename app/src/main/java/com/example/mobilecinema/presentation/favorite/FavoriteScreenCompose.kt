package com.example.mobilecinema.presentation.favorite

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.use_case.UiState
import kotlin.math.max
import kotlin.math.min

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel) {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val genres = viewModel.allFavoriteGenres.collectAsState(initial = UiState.Loading)
    val films = viewModel.favoriteMovies.collectAsState(initial = UiState.Loading)
    val rating = viewModel.moviesRating.collectAsState(initial = UiState.Loading)

    when (val state = films.value) {
        is UiState.Error -> {}
        UiState.Loading -> {}
        is UiState.Success -> {
            if (state.data.movies?.isEmpty() == true)
                Placeholder()
            else {
                Box(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.dark))
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    LazyColumn() {
                        item {
                            Text(
                                text = stringResource(id = R.string.favorites),
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                                    color = Color.White
                                ),
                            )
                        }
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                        item {
                            Text(
                                text = stringResource(id = R.string.favorite_genres),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                                    brush = Brush.horizontalGradient(
                                        listOf(startColor, endColor)
                                    )
                                )
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        when (val state1 = genres.value) {
                            is UiState.Error -> {}
                            UiState.Loading -> {}
                            is UiState.Success -> {
                                item { Genres(state1.data) }
                            }
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.favorite_films),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                                    brush = Brush.horizontalGradient(
                                        listOf(startColor, endColor)
                                    )
                                )
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        when (val state3 = films.value) {
                            is UiState.Error -> {}
                            UiState.Loading -> {}
                            is UiState.Success -> {
                                when (val state2 = rating.value) {
                                    is UiState.Error -> {}
                                    UiState.Loading -> {}
                                    is UiState.Success -> {
                                        item { Box { Films(state3.data, state2.data) } }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
    }


}

@Composable
fun Films(films: MoviesListModel, data: List<Float>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        itemsIndexed(films.movies ?: emptyList()) { index, film ->
            // Use the index if needed, otherwise just use `film`
            FilmCard(data[index], film)
        }

    }
}

@Composable
fun Genres(genres: List<GenreModel>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        items(genres.size) {
            if (it >= 3 || genres.size == it)
                return@items
            genres[it].genreName?.let { it1 -> GenreElement(it1)
            Spacer(modifier = Modifier.height(8.dp))}
        }


    }
}

@Composable
fun FilmCard(rating: Float, movie: MovieElementModel) {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.green)
    val normalizedValue = max(0f, min(rating, 10f)) / 10f
    val currentColor = interpolateColor(startColor, endColor, normalizedValue)







    Card(
        onClick = {},
        modifier = Modifier
            .padding(4.dp)
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = movie.poster,
                contentDescription = ""
            )
            Text(
                text = rating.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = currentColor)
                    .padding(8.dp, 4.dp, 8.dp, 4.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.white),
                    fontFamily = FontFamily(Font(R.font.manrope_bold))
                ),
            )
        }
    }
}








fun interpolateColor(start: Color, end: Color, fraction: Float): Color {
    val startRed = start.red * 255
    val startGreen = start.green * 255
    val startBlue = start.blue * 255

    val endRed = end.red * 255
    val endGreen = end.green * 255
    val endBlue = end.blue * 255

    val red = (startRed + (endRed - startRed) * fraction).toInt()
    val green = (startGreen + (endGreen - startGreen) * fraction).toInt()
    val blue = (startBlue + (endBlue - startBlue) * fraction).toInt()

    return Color(red, green, blue)
}

@Composable
fun GenreElement(string: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_faded))
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.dark_faded))
                        .padding(12.dp, 18.dp, 0.dp, 18.dp),
                    text = string,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.white),
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(12.dp, 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = colorResource(id = R.color.dark))
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_7),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun Placeholder() {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.dark))
            .fillMaxSize()

    )

    Image(
        bitmap = ImageBitmap.imageResource(id = R.drawable.background),
        contentDescription = "",
        modifier = Modifier
            .offset(0.dp)
            .height(screenHeight * 0.6f)
            .clip(
                RoundedCornerShape(
                    0.dp,
                    0.dp,
                    34.dp,
                    34.dp
                )
            )
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        Spacer(
            modifier = Modifier
                .height(screenHeight * 0.6f)
        )


        Text(
            text = stringResource(id = R.string.empty),
            style = TextStyle(
                fontSize = 24.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            ),
            modifier = Modifier
                .padding(top = 32.dp)
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = stringResource(id = R.string.add_favorite),
            style = TextStyle(
                fontSize = 15.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            ),
        )
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(top = 38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(startColor, endColor)
                    )
                )
        ) {
            Text(
                text = stringResource(id = R.string.find_film),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.white),
                    fontFamily = FontFamily(Font(R.font.manrope_bold))
                ),
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }


    }

}