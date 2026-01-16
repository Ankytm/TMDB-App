package com.app.tmdbtask.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun HomeScreen(
    viewModel: MoviesViewModel,
    onMovieClick: (Long) -> Unit,
    onSavedClick: () -> Unit,
    onSearch: () -> Unit
) {
    val trendingItems = viewModel.trending.collectAsLazyPagingItems()
    val nowPlayingItems = viewModel.nowPlaying.collectAsLazyPagingItems()

    if (trendingItems.itemCount == 0 && nowPlayingItems.itemCount == 0) {
        LoaderIndicator()
    } else {
        HomeScreenComponents(
            trendingItems = trendingItems,
            nowPlayingItems = nowPlayingItems,
            onMovieClick = onMovieClick,
            onSavedClick = onSavedClick,
            onSearch = onSearch
        )
    }
}

@Composable
fun HomeScreenComponents(
    trendingItems: LazyPagingItems<Movie>,
    nowPlayingItems: LazyPagingItems<Movie>,
    onMovieClick: (Long) -> Unit,
    onSavedClick: () -> Unit,
    onSearch: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FAB(onSavedClick = onSavedClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // Apply the system bar padding (status bar top, nav bar bottom)
                .padding(innerPadding)
                // Important: prevent double-padding for nested inset-aware views
                .consumeWindowInsets(innerPadding)

        ) {
            item {
                TopHeader()
            }
            item {
                SearchView(onSearch = onSearch)
            }

            item {
                HeadingComponent(
                    text = "Trending"
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
                HeadingComponent(
                    text = "Now Playing",
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
        }
    }
}

@Composable
fun FAB(onSavedClick: () -> Unit) {
    FloatingActionButton(
        onClick = onSavedClick,
        // FloatingActionButton is part of Material3
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            "Saved Movies", modifier = Modifier
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )

    }
}

@Composable
fun HeadingComponent(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
fun SearchView(onSearch: () -> Unit) {
    RoundedCornerShape(24.dp).let { shape ->

        Box(
            modifier = Modifier
                .padding(bottom = 15.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .clip(shape)
                .border(
                    width = 0.8.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = shape
                )
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    onSearch()
                }
        ) {
            Text(
                text = "Search Movies....",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(14.45.dp)
            )
        }
    }
}

@Composable
fun TopHeader() {
    Text(
        text = "Find Movies, Tv series, and more..", style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(20.dp)
    )
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
            .aspectRatio(2f / 3f)
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
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        modifier = Modifier.padding(16.dp).align(alignment = Alignment.Center),
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
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight(700)),
                        maxLines = 2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}