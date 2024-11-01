package com.example.mobilecinema.presentation.movies_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
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
        modifier = Modifier.padding(24.dp, 420.dp, 24.dp)
    ) {
        item { MoviesYears() }
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
fun Friends(){

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
                .padding(0.dp, 76.dp, 24.dp, 0.dp)
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
                .padding(24.dp, 76.dp, 0.dp, 0.dp)
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