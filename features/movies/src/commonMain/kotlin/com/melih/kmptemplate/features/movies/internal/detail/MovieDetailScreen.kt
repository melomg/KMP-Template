package com.melih.kmptemplate.features.movies.internal.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme
import com.melih.kmptemplate.core.shared.l10n.Res
import com.melih.kmptemplate.core.shared.l10n.back
import com.melih.kmptemplate.core.shared.l10n.movie_label_date
import com.melih.kmptemplate.core.shared.l10n.movie_label_overview
import com.melih.kmptemplate.core.shared.l10n.movie_label_rating
import com.melih.kmptemplate.core.shared.l10n.movie_rating
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.features.movies.internal.EmptyScreenContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MovieDetailScreen(
    movieId: Long,
    onBackClicked: () -> Unit,
    viewModel: MovieDetailViewModel = koinViewModel<MovieDetailViewModel> { parametersOf(movieId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(uiState) { state ->
        when (state) {
            is MovieDetailUiState.Error -> EmptyScreenContent(Modifier.fillMaxSize())
            is MovieDetailUiState.Loading -> EmptyScreenContent(Modifier.fillMaxSize())
            is MovieDetailUiState.Success -> MovieDetailScreen(
                movie = state.movie,
                onBackClicked = onBackClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailScreen(
    movie: Movie,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            SelectionContainer {
                Column(Modifier.padding(12.dp)) {
                    Text(movie.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(6.dp))
                    LabeledInfo(stringResource(Res.string.movie_label_date), movie.releaseDate)
                    LabeledInfo(
                        stringResource(Res.string.movie_label_rating),
                        stringResource(Res.string.movie_rating, movie.voteAverage, movie.voteCount),
                    )
                    LabeledInfo(stringResource(Res.string.movie_label_overview), movie.overview)
                }
            }
        }
    }
}

@Composable
private fun LabeledInfo(
    label: String,
    data: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = 4.dp)) {
        Spacer(Modifier.height(6.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$label: ")
                }
                append(data)
            }
        )
    }
}

@Composable
@PreviewScreenSizes
@PreviewLightDark
private fun MovieDetailScreenPreview() {
    AppTheme {
        MovieDetailScreen(
            movie = Movie(
                adult = false,
                backdropUrl = "w300/rMvPXy8PUjj1o8o1pzgQbdNCsvj.jpg",
                id = 299054,
                originalLanguage = "en",
                originalTitle = "Expend4bles",
                overview = "Armed with every weapon they can get their hands on and the skills to" +
                        " use them, The Expendables are the world’s last line of defense and " +
                        "the team that gets called when all other options are off the table. " +
                        "But new team members with new styles and tactics are going to " +
                        "give “new blood” a whole new meaning.",
                popularity = 2919.27f,
                posterUrl = "w342/mOX5O6JjCUWtlYp5D8wajuQRVgy.jpg",
                releaseDate = "15 Sep 2023",
                title = "Expend4bles",
                video = false,
                voteAverage = "6.4",
                voteCount = 307,
            ),
            onBackClicked = {},
        )
    }
}
