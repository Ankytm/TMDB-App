package com.app.tmdbtask.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.tmdbtask.R
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

@Composable
fun SavedScreen(viewModel: MoviesViewModel, onBackClick: () -> Unit, onDetailsScree: (Long) -> Unit) {
    val bookmarks = viewModel.bookmarks.collectAsState()

    Scaffold ( topBar = {
        BackButton(
            modifier = Modifier.padding(top = 40.dp).padding(horizontal = 24.dp),
            onClick = onBackClick
        )
    }
    ) { innerPadding ->
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            // Important: prevent double-padding for nested inset-aware views
            .consumeWindowInsets(innerPadding)
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

                    RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp).apply {
                        GlassContainer(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .clip(shape = this)
                                .clickable {
                                    viewModel.onBookmarkClicked(movie)
                                },
                            shape = this
                        ) {
                            Text(
                                "Remove from watchlist",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
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
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
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
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight(700))
            )
        }
    }
}
