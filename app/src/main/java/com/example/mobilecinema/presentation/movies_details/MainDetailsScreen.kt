package com.example.mobilecinema.presentation.movies_details

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.auth.UserShortModel
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.review.ReviewModel
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.domain.use_case.UiState
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun MoviesDetailsScreen(vm: MoviesDetailViewModel) {

    TopView()
    MoviesInfo(vm)
}


@Composable
fun MoviesInfo(vm: MoviesDetailViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var review by remember {
        mutableStateOf<ReviewModifyModel?>(null)
    }



    val film by vm.film.collectAsState()

    when (film) {
        is UiState.Error -> {
            Log.d("MoviesDetail", (film as UiState.Error<Film>).errorMessage)
        }

        UiState.Loading -> {
            CircularProgressIndicator()
        }

        is UiState.Success -> {
            LazyColumn(
                modifier = Modifier.padding(24.dp, 395.dp, 24.dp)
            ) {
                item { MoviesYears(vm.detail?.year ?: 0) }
                item { Description(vm.detail?.description ?: "") }
                item {
                    (film as UiState.Success<Film>).data.ratingKinopoisk?.let {
                        (film as UiState.Success<Film>).data.ratingImdb?.let { it1 ->
                            Rating(
                                vm.ratingCinema, it, it1
                            )
                        }
                    }
                }
                item {
                    (film as UiState.Success<Film>).data.year?.let {
                        (film as UiState.Success<Film>).data.filmLength?.let { it1 ->
                            (film as UiState.Success<Film>).data.ratingAgeLimits?.let { it2 ->
                                vm.detail?.country?.let { it3 ->
                                    Info(
                                        it3, it2, it1, it
                                    )
                                }
                            }
                        }
                    }
                }
                item { vm.detail?.director?.let { Producer(it) } }
                item {
                    vm.detail?.let {
                        Genres(
                            it.genres
                        )
                    }
                }
                item { Finance(vm.detail?.budget.toString(), vm.detail?.fees.toString()) }
                item { vm.detail?.reviews?.let { Reviews(it) } }
            }
            ReviewsDialog(showDialog = showDialog, onDismiss = {
                showDialog = false
            }, onSend = {
                review = it
            })
        }
    }

}

@Composable
fun MoviesYears(year: Int = 1899, slogan: String = "What is lost will be found") {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)

    Card(
        modifier = Modifier.fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
        ) {
            Column {


                Text(
                    text = year.toString(),
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp),
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
                Text(
                    text = slogan,
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 16.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.manrope_medium))
                    )
                )
            }
        }
    }
}

@Composable
fun Friends() {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

@Composable
fun Description(description: String = "Группа европейских мигрантов покидает Лондон на пароходе, чтобы начать новую жизнь в Нью-Йорке. Когда они сталкиваются с другим судном, плывущим по течению в открытом море, их путешествие превращается в кошмар") {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = description, style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.manrope_medium))
                )
            )
        }
    }
}

@Composable
fun Rating(cinemaRating: Float, kinopoisRating: Float, imdb: Float) {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)

    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp, 16.dp, 68.dp, 16.dp)
                    .wrapContentSize()
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon),
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.rating),
                    modifier = Modifier.padding(start = 4.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.gray),
                        fontFamily = FontFamily(Font(R.font.manrope_medium))
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp, 48.dp, 16.dp, 16.dp)
                    .wrapContentSize()
                    .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1.5f)
                        .background(
                            color = colorResource(id = R.color.dark)
                        )
                ) {


                    Row(
                        modifier = Modifier, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .height(24.dp)
                        )
                        Text(
                            text = cinemaRating.toString(), style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.manrope_bold))
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1f)
                        .background(
                            color = colorResource(id = R.color.dark)
                        )
                ) {
                    Row(
                        modifier = Modifier, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.kinopoisk),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .size(24.dp)

                        )
                        Text(
                            text = kinopoisRating.toString(), style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.manrope_bold))
                            )
                        )

                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1f)
                        .background(
                            color = colorResource(id = R.color.dark)
                        )
                ) {
                    Row(
                        modifier = Modifier, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.imdb_logo_square),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = imdb.toString(), style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.manrope_bold))
                            )
                        )

                    }
                }
            }
        }

    }
}


@Composable
fun Genres(
    listName: List<GenreModel?>,
    listFavorite: List<Boolean> = listOf(true, false, false),
) {
    val backgroundColor = colorResource(id = R.color.dark_faded)


    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentSize()


    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor)
                .wrapContentSize()
        ) {
            Column {


                Row(
                    modifier = Modifier
                        .padding(16.dp, 16.dp, 68.dp, 8.dp)
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_2),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(id = R.string.genres),
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.gray),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        )
                    )
                }

                FlowRow(
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                ) {
                    listName.forEachIndexed { index, _ ->
                        listName[index]?.genreName?.let { GenreElement(it, listFavorite[index]) }
                    }
                }


            }
        }
    }
}

@Composable
fun Finance(budget: String, feel: String) {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentSize()


    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor)
                .wrapContentSize()
        ) {
            Column {


                Row(
                    modifier = Modifier
                        .padding(16.dp, 16.dp, 68.dp, 8.dp)
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_1),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(id = R.string.finance),
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.gray),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        )
                    )
                }
                Row(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {
                        InfoElement(
                            res = R.string.budget, "\$ $budget"
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {
                        InfoElement(
                            res = R.string.feel_in_world, "\$ $feel"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreElement(genre: String = "фантастикаasd", isFavorite: Boolean = false) {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val commonColor = colorResource(id = R.color.dark)
    Box(
        modifier = if (isFavorite) Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(startColor, endColor)
                )
            )
            .padding(12.dp, 8.dp, 12.dp, 8.dp)
            .wrapContentSize()
        else Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(commonColor)
            .padding(12.dp, 8.dp, 12.dp, 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .wrapContentSize()

    ) {
        Text(
            text = genre, style = TextStyle(
                fontSize = 16.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_medium))
            )
        )
    }
}

@Composable
fun Producer(name: String) {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentSize()


    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor)
                .wrapContentSize()
        ) {
            Column {


                Row(
                    modifier = Modifier
                        .padding(16.dp, 16.dp, 68.dp, 8.dp)
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_1),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(id = R.string.producer),
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.gray),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = colorResource(id = R.color.dark)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.dark))
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.temp2),
                            contentDescription = "",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = name, style = TextStyle(
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.white),
                                fontFamily = FontFamily(Font(R.font.manrope_medium))
                            ), modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Info(
    country: String = "Германия, США",
    age: String = "16+",
    time: String = "1ч 30мин",
    year: Int = 2022,
) {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 16.dp)

    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor)
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp, 16.dp, 68.dp, 16.dp)
                    .wrapContentSize()
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.shape),
                    contentDescription = ""
                )
                Text(
                    text = stringResource(id = R.string.info),
                    modifier = Modifier.padding(start = 4.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.gray),
                        fontFamily = FontFamily(Font(R.font.manrope_medium))
                    )
                )
            }
            Column {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 48.dp, 16.dp, 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(3f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {
                        InfoElement(R.string.country, country)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {
                        InfoElement(R.string.age, age)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {


                        InfoElement(R.string.time, time)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .background(
                                color = colorResource(id = R.color.dark)
                            )
                    ) {
                        InfoElement(R.string.years, year.toString())
                    }
                }
            }
        }

    }
}

@Composable
fun Reviews(
    reviews: List<ReviewModel> = listOf(
        ReviewModel(
            id = "1",
            rating = 10,
            reviewText = "Если у вас взорвался мозг от «Тьмы» и вам это понравилось, то не переживайте. Новый сериал Барана бо Одара «1899» получился не хуже предшественника",
            isAnonymous = true,
            createDateTime = "17 октября 2024",
            author = UserShortModel(
                userId = "1",
                nickName = "Ahmed",
                avatar = "https://avatars.mds.yandex.net/i?id=c7c257a3c0de2fdef41a868e366ce8a997cd32d9-5283698-images-thumbs&n=13"
            )

        ),
        ReviewModel(
            id = "1",
            rating = 8,
            reviewText = " и вам это понравилось, то не переживайте. Новый сериал Барана бо Одара «1899» получился не хуже предшественника",
            isAnonymous = false,
            createDateTime = "17 октября 2020",
            author = UserShortModel(
                userId = "1",
                nickName = "Ahmed",
                avatar = "https://avatars.mds.yandex.net/i?id=02a0d438915e4409b6779abb9faf64f6cfcca7e5-5380211-images-thumbs&n=13"
            )

        ),
    ),
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        reviews.size
    })

    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 16.dp)

    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp, 16.dp, 12.dp, 0.dp)
                    .wrapContentSize()
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.shape_3),
                    contentDescription = ""
                )
                Text(
                    text = stringResource(id = R.string.reviews),
                    modifier = Modifier.padding(start = 4.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.gray),
                        fontFamily = FontFamily(Font(R.font.manrope_medium))
                    )
                )
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxWidth()
            ) { page ->
                ReviewElement(reviews[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(startColor, endColor)
                            )
                        )
                        .weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.add_review))
                }
                Spacer(
                    modifier = Modifier.width(24.dp)
                )
                IconButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.scrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = colorResource(id = R.color.dark_faded)
                        ),
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_6),
                        contentDescription = ""
                    )
                }


                IconButton(
                    onClick = {
                        if (pagerState.currentPage + 1 < reviews.size) {
                            coroutineScope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = colorResource(id = R.color.dark)
                        )
                        .wrapContentSize()
                        .padding(0.dp),
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.shape_5),
                        contentDescription = ""
                    )
                }
            }

        }
    }
}

@Composable
fun ReviewsDialog(
    showDialog: Boolean = true,
    onDismiss: () -> Unit,
    onSend: (ReviewModifyModel) -> Unit,
) {
    if (showDialog) Dialog(onDismissRequest = onDismiss) {
        DialogContent {
            onSend(it)
        }
    }
}

@Composable
fun DialogContent(callback: (ReviewModifyModel) -> Unit) {
    val review = remember {
        mutableStateOf(
            ReviewModifyModel("", 5, true)
        )
    }


    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.dark))
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.add_review), style = TextStyle(
                fontSize = 20.sp,
                color = colorResource(id = R.color.white),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            )
        )
        Text(
            text = stringResource(id = R.string.evaluation), style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray),
                fontFamily = FontFamily(Font(R.font.manrope_medium))
            ), modifier = Modifier.padding(top = 24.dp)
        )
        CustomSlider {
            review.value = review.value.copy(rating = it)
        }

        BasicTextField(
            value = review.value.reviewText,
            onValueChange = { review.value = review.value.copy(reviewText = it) },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(id = R.color.dark_faded))
                .fillMaxWidth()
                .height(120.dp)
        )
        Row(
            modifier = Modifier.padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.anonymous_reviews),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.gray),
                    fontFamily = FontFamily(Font(R.font.manrope_medium))
                ),
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = review.value.isAnonymous, onCheckedChange = {
                    review.value = review.value.copy(isAnonymous = it)
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.white),
                    checkedTrackColor = colorResource(id = R.color.gradient_1),
                    uncheckedThumbColor = colorResource(id = R.color.white),
                    uncheckedTrackColor = colorResource(id = R.color.dark_faded)
                )

            )
        }
        Row(
            modifier = Modifier.padding(top = 24.dp)
        ) {

            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { callback(review.value) }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(startColor, endColor)
                        )
                    )
                    .wrapContentSize()
            ) {
                Text(
                    text = stringResource(id = R.string.send),
                    modifier = Modifier.padding(24.dp, 10.dp)
                )
            }
        }
    }
}


@Composable
fun CustomSlider(setResult: (Int) -> Unit) {
    var sliderPosition by remember { mutableIntStateOf(5) }
    val thumbRadius = 22.dp
    val trackHeight = 16.dp
    val circleRadius = 2.dp
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)
    val darkFadedColor = colorResource(id = R.color.dark_faded)
    val points = List(11) { it * 0.1f }
    val thumbCornerRadius = 6.dp
    val cornerRadius = 10.dp
    val thumbPadding = 6.dp
    val sliderStartY = 60.dp
    val sliderEndY = trackHeight + sliderStartY
    val sliderCenter = (sliderStartY + sliderEndY) / 2

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .background(
            color = colorResource(id = R.color.dark)
        )
        .pointerInput(Unit) {
            detectDragGestures { change, _ ->
                change.consume()
                val newPosition = (change.position.x / size.width).coerceIn(0f, 1f)
                val closestPoint = points.indices.minByOrNull {
                    abs(it * 0.1f - newPosition)
                } ?: 0
                sliderPosition = closestPoint
                setResult(sliderPosition)
            }
        }) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val sliderY = sliderCenter.toPx()


            val trackStart = Offset(0f, sliderY)
            val trackEnd = Offset(size.width, sliderY)
            val sliderX = (sliderPosition * (trackEnd.x - trackStart.x)) / 10

            val gradientBrush = Brush.linearGradient(
                colors = listOf(startColor, endColor),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height)
            )
            var path: Path

            if (sliderPosition != 0) {
                path = Path().apply {
                    moveTo(0f, sliderY)
                    quadraticTo(
                        0f, sliderStartY.toPx(), cornerRadius.toPx(), sliderStartY.toPx()
                    )
                    lineTo(
                        sliderX - thumbPadding.toPx() - thumbCornerRadius.toPx(),
                        sliderStartY.toPx()
                    )
                    quadraticTo(
                        sliderX - thumbPadding.toPx(),
                        sliderStartY.toPx(),
                        sliderX - thumbPadding.toPx(),
                        sliderStartY.toPx() + thumbCornerRadius.toPx()
                    )
                    lineTo(
                        sliderX - thumbPadding.toPx(), sliderEndY.toPx() - thumbCornerRadius.toPx()
                    )
                    quadraticTo(
                        sliderX - thumbPadding.toPx(),
                        sliderEndY.toPx(),
                        sliderX - thumbPadding.toPx() - thumbCornerRadius.toPx(),
                        sliderEndY.toPx()
                    )
                    lineTo(
                        cornerRadius.toPx(), sliderEndY.toPx()
                    )
                    quadraticTo(
                        0f, sliderEndY.toPx(), 0f, sliderY
                    )
                    close()
                }



                drawPath(
                    path = path, brush = gradientBrush, style = Fill
                )

            }

            path = Path().apply {
                moveTo(
                    sliderX, sliderY + 22.dp.toPx()
                )
                lineTo(
                    sliderX, sliderY - 22.dp.toPx()
                )
            }



            drawPath(
                path = path, brush = gradientBrush, style = Stroke(
                    width = 2.dp.toPx(), cap = StrokeCap.Round
                )
            )

            drawCircle(
                color = darkFadedColor,
                radius = thumbRadius.toPx(),
                center = Offset(sliderX, 22.dp.toPx())
            )
            drawContext.canvas.nativeCanvas.apply {
                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 14.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawText(
                    sliderPosition.toString(),
                    sliderX,
                    22.dp.toPx() + (textPaint.textSize / 3),
                    textPaint
                )
            }

            for (i in 0..<sliderPosition) {
                drawCircle(
                    color = Color.White, radius = circleRadius.toPx(), center = Offset(
                        ((i + 0.2f) * (trackEnd.x - trackStart.x)) / 10, sliderCenter.toPx()
                    )
                )
            }
            if (sliderPosition != 10) {
                path = Path().apply {
                    moveTo(size.width, sliderY)
                    quadraticTo(
                        size.width,
                        sliderStartY.toPx(),
                        size.width - cornerRadius.toPx(),
                        sliderStartY.toPx()
                    )
                    lineTo(
                        sliderX + thumbPadding.toPx() + thumbCornerRadius.toPx(),
                        sliderStartY.toPx()
                    )
                    quadraticTo(
                        sliderX + thumbPadding.toPx(),
                        sliderStartY.toPx(),
                        sliderX + thumbPadding.toPx(),
                        sliderStartY.toPx() + thumbCornerRadius.toPx()
                    )
                    lineTo(
                        sliderX + thumbPadding.toPx(), sliderEndY.toPx() - thumbCornerRadius.toPx()
                    )
                    quadraticTo(
                        sliderX + thumbPadding.toPx(),
                        sliderEndY.toPx(),
                        sliderX + thumbPadding.toPx() + thumbCornerRadius.toPx(),
                        sliderEndY.toPx()
                    )
                    lineTo(
                        size.width - cornerRadius.toPx(), sliderEndY.toPx()
                    )
                    quadraticTo(
                        size.width, sliderEndY.toPx(), size.width, sliderY
                    )
                    close()
                }
                drawPath(
                    path = path, color = darkFadedColor, style = Fill
                )
            }
            for (i in sliderPosition + 1..10) {
                drawCircle(
                    brush = gradientBrush, radius = circleRadius.toPx(), center = Offset(
                        (i * (trackEnd.x - trackStart.x)) / 10.3f, sliderCenter.toPx()
                    )
                )
            }
        }

    }
}

@Composable
fun ReviewElement(reviewModel: ReviewModel) {
    val painter = rememberAsyncImagePainter(reviewModel.author.avatar)

    Box(
        modifier = Modifier
            .padding(16.dp, 10.dp, 16.dp, 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.dark))
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp)
            ) {
                if (reviewModel.isAnonymous) Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ellipse_22),
                    contentDescription = ""
                )
                else {

                    when (painter.state) {
                        is AsyncImagePainter.State.Empty,
                        is AsyncImagePainter.State.Loading,
                        -> {
                            CircularProgressIndicator()
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter, contentDescription = ""
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Image(
                                bitmap = ImageBitmap.imageResource(id = R.drawable.ellipse_22),
                                contentDescription = ""
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = if (reviewModel.isAnonymous) stringResource(id = R.string.anonymous)
                        else reviewModel.author.nickName ?: "", style = TextStyle(
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.white),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        ), maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = reviewModel.createDateTime,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.gray_faded),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        ),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = colorResource(id = R.color.green)),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon),
                        contentDescription = "",
                        modifier = Modifier.padding(4.dp, 4.dp, 4.dp, 4.dp)
                    )

                    Text(
                        text = reviewModel.rating.toString(), style = TextStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.white),
                            fontFamily = FontFamily(Font(R.font.manrope_medium))
                        ), modifier = Modifier.padding(0.dp, 4.dp, 4.dp, 4.dp)
                    )
                }
            }
            Text(
                text = reviewModel.reviewText ?: "", style = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.white),
                    fontFamily = FontFamily(Font(R.font.manrope_medium))
                ), modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 12.dp)
            )
        }
    }
}

@Composable
fun InfoElement(res: Int, value: String = "16+") {

    Column(
        modifier = Modifier, horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = res),
            modifier = Modifier.padding(start = 12.dp, top = 8.dp),
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            )
        )


        Text(
            text = value,
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            )
        )

    }
}

@Composable
fun TopView() {
    Box(
        modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = ColorPainter(
                Color(
                    ContextCompat.getColor(
                        LocalContext.current, R.color.dark
                    )
                )
            ),
            contentDescription = "movies",
        )

        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = ImageBitmap.imageResource(id = R.drawable.sub_container_1),
            contentDescription = "Movies",
            alignment = Alignment.TopStart,
            contentScale = ContentScale.FillWidth
        )

        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(0.dp, 60.dp, 24.dp, 0.dp)
                .size(50.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.frame_174),
                contentDescription = "asd",
                modifier = Modifier.size(40.dp, 40.dp)

            )
        }

        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp, 60.dp, 0.dp, 0.dp)
                .size(50.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.button),
                contentDescription = "asd",
                modifier = Modifier.size(40.dp, 40.dp)

            )
        }
    }
}