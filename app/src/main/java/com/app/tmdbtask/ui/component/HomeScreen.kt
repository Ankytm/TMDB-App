package com.app.tmdbtask.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun HomeScreen(
    viewModel: MoviesViewModel, onMovieClick: (Long) -> Unit, onSavedClick: () -> Unit, onSearch: () -> Unit
) {
    val trendingItems = viewModel.trending.collectAsLazyPagingItems()
    val nowPlayingItems = viewModel.nowPlaying.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {

        if (trendingItems.itemCount == 0 && nowPlayingItems.itemCount == 0) {

            item {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp),) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            }
        } else {

            item {
                RoundedCornerShape(12.dp).let { shape ->
                    Box(
                        modifier = Modifier.padding(bottom = 10.dp, start = 20.dp, end = 20.dp).fillMaxWidth()
                            .clip(shape)
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = shape
                            ).clickable{
                                onSearch()
                            }
                    ) {
                        Text(
                            text = "Search Movies....",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Trending",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                LazyRow {
                    items(trendingItems.itemCount) { index ->
                        trendingItems[index]?.let { movie ->
                            MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                LazyRow {
                    items(nowPlayingItems.itemCount) { index ->
                        nowPlayingItems[index]?.let { movie ->
                            MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = onSavedClick,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Saved Movies")
                }

            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    removeBottomRadius: Boolean = false
) {
    (if (removeBottomRadius) RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp
    ) else RoundedCornerShape(20.dp)).let { cornerShape ->
        val paddingValues = if (removeBottomRadius) PaddingValues(
            top = 8.dp, start = 8.dp, end = 8.dp
        ) else PaddingValues(8.dp)
        val widthModifier = if (removeBottomRadius) modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .height(320.dp) else modifier
            .padding(paddingValues)
            .width(240.dp)
            .height(320.dp)
        Card(
            modifier = widthModifier
                .clip(shape = cornerShape)
                .clickable { onClick() },
            shape = cornerShape

        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                GlassContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.BottomCenter),
                    shape = if (removeBottomRadius) RoundedCornerShape(0.dp) else RoundedCornerShape(
                        bottomStart = 20.dp, bottomEnd = 20.dp
                    )
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                        maxLines = 2,
                        modifier = Modifier.padding(16.dp),
                    )

                }

                GlassContainer(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(alignment = Alignment.TopEnd),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "⭐ ${String.format("%.1f", movie.voteAverage)}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}