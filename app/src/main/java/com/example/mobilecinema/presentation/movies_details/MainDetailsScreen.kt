package com.example.mobilecinema.presentation.movies_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mobilecinema.R

@Composable
fun MoviesDetailsScreen() {

    TopView()
    MoviesInfo()
}


@Composable
fun MoviesInfo() {
    LazyColumn(
        modifier = Modifier.padding(24.dp, 395.dp, 24.dp)
    ) {
        item { MoviesYears() }
        item { Description() }
        item { Rating() }
        item { Info() }
        item { Producer() }
    }
}

@Composable
fun MoviesYears() {
    val startColor = colorResource(id = R.color.gradient_1)
    val endColor = colorResource(id = R.color.gradient_2)

    Card(
        modifier = Modifier
            .fillMaxSize()

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
                    text = "1899",
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp),
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
                Text(
                    text = "What is lost will be found",
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
        modifier = Modifier
            .fillMaxSize()
    ) {

    }
}

@Composable
fun Description() {
    val backgroundColor = colorResource(id = R.color.dark_faded)
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = "Группа европейских мигрантов покидает Лондон на пароходе, чтобы начать новую жизнь в Нью-Йорке. Когда они сталкиваются с другим судном, плывущим по течению в открытом море, их путешествие превращается в кошмар",

                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.manrope_medium))
                )
            )
        }
    }
}

@Composable
fun Rating() {
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
                    modifier = Modifier
                        .padding(start = 4.dp),
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
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween

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
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .height(24.dp)
                        )
                        Text(
                            text = "9.9",
                            style = TextStyle(
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
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.kinopoisk),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .size(24.dp)

                        )
                        Text(
                            text = "9.9",
                            style = TextStyle(
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
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.imdb_logo_square),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp, 9.46.dp, 8.dp, 9.46.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = "9.9",
                            style = TextStyle(
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

@Preview(showSystemUi = true)

@Composable
fun Producer() {
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
                        modifier = Modifier
                            .padding(start = 4.dp),
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
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                            text = "Баран бо Одар",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.white),
                                fontFamily = FontFamily(Font(R.font.manrope_medium))
                            ),
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Info() {
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
                    modifier = Modifier
                        .padding(start = 4.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.gray),
                        fontFamily = FontFamily(Font(R.font.manrope_medium))
                    )
                )
            }
            Column(

            ) {
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
                        InfoElement(R.string.country)
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
                        InfoElement(R.string.age)
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


                        InfoElement(R.string.time)
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
                        InfoElement(R.string.years)
                    }
                }
            }
        }

    }
}

@Composable
fun InfoElement(res: Int) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = res),
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp),
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray),
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            )
        )


        Text(
            text = "16+",
            modifier = Modifier
                .padding(start = 12.dp, bottom = 8.dp),
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