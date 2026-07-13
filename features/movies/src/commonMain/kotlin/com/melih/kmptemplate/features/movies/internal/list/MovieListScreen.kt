package com.melih.kmptemplate.features.movies.internal.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.features.movies.internal.EmptyScreenContent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieListScreen(
    onMovieDetailClicked: (movieId: Long) -> Unit,
    viewModel: MovieListViewModel = koinViewModel<MovieListViewModel>(),
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()

    AnimatedContent(movies.isNotEmpty()) { objectsAvailable ->
        if (objectsAvailable) {
            MoviesGrid(
                movies = movies,
                onMovieDetailClicked = onMovieDetailClicked,
                contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
            )
        } else {
            EmptyScreenContent(Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    onMovieDetailClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieItem(
                movie = movie,
                onClick = { onMovieDetailClicked(movie.id) },
            )
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(Modifier.height(2.dp))

        Text(movie.title, style = MaterialTheme.typography.titleMedium)
        Text(movie.voteAverage, style = MaterialTheme.typography.bodyMedium)
        Text(movie.releaseDate, style = MaterialTheme.typography.bodySmall)
    }
}
