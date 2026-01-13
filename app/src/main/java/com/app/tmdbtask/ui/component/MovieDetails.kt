package com.app.tmdbtask.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.tmdbtask.R
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun DetailsScreen(
    movieId: Long, viewModel: MoviesViewModel, onSavedClick: () -> Unit
) {

    val movieDetails by viewModel.movieDetails.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    when (val movie = movieDetails) {
        null -> CircularProgressIndicator()
        else -> MovieDetail(movie = movie, onSavedClick = {
            onSavedClick()
        }) {
            viewModel.onBookmarkClicked(movie)
        }
    }

}

@Composable
fun MovieDetail(movie: Movie, onSavedClick: () -> Unit , onBookmarkClicked: () -> Unit) {
    var isBookmarked by remember { mutableStateOf(movie.isBookmarked) }

    LazyColumn {
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
                Card (
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),

                ) {
                    IconToggleButton(
                        modifier = Modifier
                            .padding(20.dp),
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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            Text(movie.overview, style = MaterialTheme.typography.bodyMedium ,modifier = Modifier.padding(16.dp))
        }
        item {
            Text("Released on: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
        }

        item {
            Button(onClick = onSavedClick, modifier = Modifier.padding(top = 8.dp, bottom = 20.dp, start = 8.dp, end = 8.dp).fillMaxWidth()) {
                Text("Saved Movies")
            }

        }
    }
}