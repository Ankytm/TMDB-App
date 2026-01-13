package com.app.tmdbtask.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.tmdbtask.R
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun SavedScreen(viewModel: MoviesViewModel, onDetailsScree: (Long) -> Unit) {
    val bookmarks = viewModel.bookmarks.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        when {
            bookmarks.value.isEmpty() -> {
                item {
                    EmptyResult()
                }
            }

            else -> {

                items(bookmarks.value) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onDetailsScree(movie.id) },
                        removeBottomRadius = true
                    )

                    GlassContainer(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onBookmarkClicked(movie)
                            },
                        shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
                    ) {
                        Text(
                            "Remove from watchlist", style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun EmptyResult(message: String = "No results found") {
    RoundedCornerShape(12.dp).let { shape ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .padding(16.dp)
                .clip(shape = shape)
                .animatedBorder(
                    backgroundColor = Color.White,
                    shape = shape
                ).padding(
                    20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(132.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
