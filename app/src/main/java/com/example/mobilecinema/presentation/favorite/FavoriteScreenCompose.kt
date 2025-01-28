package com.example.mobilecinema.presentation.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.presentation.ColorHelper
import com.example.mobilecinema.presentation.movies_details.MoviesDetailsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.max
import kotlin.math.min

private var startColor: Color? = null
private var endColor: Color? = null

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    navController: NavController,
    bottomBar: BottomNavigationView,
) {
    startColor = colorResource(id = R.color.gradient_1)
    endColor = colorResource(id = R.color.gradient_2)
    val genres = viewModel.allFavoriteGenres.collectAsState(initial = UiState.Loading)
    val films = viewModel.favoriteMovies.collectAsState(initial = UiState.Loading)
    val rating = viewModel.moviesRating.collectAsState(initial = UiState.Loading)
    val isNotEmpty = viewModel.haveAny.collectAsState(initial = null)
    val context = LocalContext.current
    when (isNotEmpty.value) {
        true -> {
            if (genres.value is UiState.Success<List<GenreModel>>
                && films.value is UiState.Success<MoviesListModel>
                && rating.value is UiState.Success<List<Float>>
            )
                MainScreen(

                    (genres.value as UiState.Success<List<GenreModel>>).data,
                    (films.value as UiState.Success<MoviesListModel>).data,
                    (rating.value as UiState.Success<List<Float>>).data,
                    onClickGenre = {
                        viewModel.deleteFavoriteGenre(it)
                    },
                    onClickFilm = {

                        context.navigateToActivity(MoviesDetailsActivity::class.java, it.id)
                    }
                )
            else
                LoaderMain()
        }

        false -> {
            Placeholder(onClick = {
                bottomBar.selectedItemId = R.id.navigation_feedScreen
            })
        }

        else -> {
            LoaderMain()

        }
    }


}

fun Context.navigateToActivity(targetActivity: Class<out Activity>, data: String) {
    val intent = Intent(this, targetActivity).apply {
        putExtra("filmId", data)
    }
    startActivity(intent)
}

@Composable
fun LoaderMain(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun MainScreen(
    genres: List<GenreModel>,
    films: MoviesListModel,
    rating: List<Float>,
    onClickGenre: (GenreModel) -> Unit,
    onClickFilm: (MovieElementModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.dark))
            .fillMaxSize()
            .padding(24.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            item { Label() }

            item { FavoritesGenres(genres, onClickGenre) }

            item { FavoritesFilms(films, rating, onClickFilm) }
        }
    }
}

@Composable
fun Label() {
    Column {
        Text(
            text = stringResource(id = R.string.favorites),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White
            ),
        )
    }

}


@Composable
fun FavoritesGenres(genres: List<GenreModel>, onClickGenre: (GenreModel) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.favorite_genres), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                brush = Brush.horizontalGradient(
                    listOf(startColor!!, endColor!!)
                )
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Genres(genres, onClickGenre)
    }
}

@Composable
fun FavoritesFilms(
    films: MoviesListModel,
    rating: List<Float>,
    onClick: (MovieElementModel) -> Unit,
) {
    Column {
        Text(
            text = stringResource(id = R.string.favorite_films), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                brush = Brush.horizontalGradient(
                    listOf(startColor!!, endColor!!)
                )
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box { Films(films, rating, onClick) }
    }
}

@Composable
fun Films(films: MoviesListModel, data: List<Float>, onClick: (MovieElementModel) -> Unit) {
    val columns = 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        films.movies?.chunked(columns)?.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEachIndexed { index, film ->
                    FilmCard(data[index], film, onClick)
                }
                if (rowItems.size < columns) {
                    repeat(columns - rowItems.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f, fill = true)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Genres(genres: List<GenreModel>, onClick: (GenreModel) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        genres.forEach { genre ->

            GenreElement(genre, onClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FilmCard(rating: Float, movie: MovieElementModel, onClick: (MovieElementModel) -> Unit) {


    val currentColor = colorResource(ColorHelper.getColor(rating.toInt()))

    Card(
        onClick = { onClick(movie) }, modifier = Modifier.padding(4.dp)
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = movie.poster, contentDescription = ""
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

@Composable
fun GenreElement(genre: GenreModel, onClick: (GenreModel) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_faded))
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.dark_faded))
                        .padding(12.dp, 18.dp, 0.dp, 18.dp),
                    text = genre.genreName ?: "",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.white),
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { onClick(genre) },
                    modifier = Modifier
                        .padding(12.dp, 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = colorResource(id = R.color.dark))
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_7),
                        contentDescription = "",
                    )
                }
            }
        }
    }
}

@Composable
fun Placeholder(onClick: () -> Unit = {}) {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.dark))
            .fillMaxSize()

    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.background),
            contentDescription = "",
            modifier = Modifier
                .offset(0.dp)
                .height(500.dp)
                .clip(
                    RoundedCornerShape(
                        0.dp, 0.dp, 34.dp, 34.dp
                    )
                )
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }


    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(screenHeight * 0.6f)
        )


        Text(
            text = stringResource(id = R.string.empty), style = TextStyle(
                fontSize = 24.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            ), modifier = Modifier.padding(top = 32.dp)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.add_favorite),
            style = TextStyle(
                fontSize = 15.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            ),
        )
        Button(
            onClick = { onClick() }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ), modifier = Modifier
                .padding(top = 38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(startColor, endColor)
                    )
                )
        ) {
            Text(
                text = stringResource(id = R.string.find_film), style = TextStyle(
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.white),
                    fontFamily = FontFamily(Font(R.font.manrope_bold))
                ), modifier = Modifier.padding(vertical = 10.dp)
            )
        }


    }

}