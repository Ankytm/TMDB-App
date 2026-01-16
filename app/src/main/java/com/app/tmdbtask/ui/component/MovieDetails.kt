package com.app.tmdbtask.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.tmdbtask.R
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun DetailsScreen(
    movieId: Long, viewModel: MoviesViewModel, onBackClick: () -> Unit, onSavedClick: () -> Unit
) {

    val movieDetails by viewModel.movieDetails.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    when (val movie = movieDetails) {
        null -> CircularProgressIndicator()
        else -> MovieDetail(movie = movie, onSavedClick = {
            onSavedClick()
        }, onBackClick = onBackClick) {
            viewModel.onBookmarkClicked(movie)
        }
    }

}

@Composable
fun MovieDetail(movie: Movie, onSavedClick: () -> Unit , onBackClick: () -> Unit , onBookmarkClicked: () -> Unit) {
    var isBookmarked by remember { mutableStateOf(movie.isBookmarked) }
    Scaffold(
        topBar = {
            BackButton(
                modifier = Modifier.padding(vertical = 40.dp, horizontal = 24.dp),
                onClick = onBackClick
            )
        },
        floatingActionButton = {
            FAB(onSavedClick = onSavedClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                // Important: prevent double-padding for nested inset-aware views
            .consumeWindowInsets(innerPadding)
        ) {
            item {
                Box {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .defaultMinSize(minHeight = 240.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    GlassContainer(
                        modifier = Modifier
                            .padding(top = 48.dp, end = 20.dp)
                            .align(alignment = Alignment.TopEnd),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "⭐ ${String.format("%.1f", movie.voteAverage)}",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight(700)),
                            maxLines = 2,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Card(
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.BottomEnd).padding(20.dp),

                        ) {
                        IconToggleButton(
                            modifier = Modifier
                                .padding(12.dp),
                            checked = isBookmarked,
                            onCheckedChange = {
                                onBookmarkClicked()
                                isBookmarked = !isBookmarked
                            }) {
                            Icon(
                                painter = painterResource(if (isBookmarked) R.drawable.ic_filled_bookmark else R.drawable.ic_outlined_bookmark),
                                contentDescription = "Bookmark"
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Text(
                    movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Text(
                    "Released on: ${movie.releaseDate}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun BackButton(modifier: Modifier, onClick: () -> Unit) {
    RoundedCornerShape(24.dp).apply {
        GlassContainer(
            modifier = modifier
                .clip(shape = this)
                .clickable(onClick = onClick)
                .padding(6.dp),
            shape = this
        ) {
            Icon(painterResource(R.drawable.ic_back), contentDescription = null)
        }
    }
}