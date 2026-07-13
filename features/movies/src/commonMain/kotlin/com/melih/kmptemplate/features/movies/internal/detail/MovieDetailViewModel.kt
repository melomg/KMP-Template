package com.melih.kmptemplate.features.movies.internal.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class MovieDetailViewModel(
    movieId: Long,
    moviesRepository: MoviesRepository,
) : ViewModel() {

    val uiState = moviesRepository.getMovieById(movieId)
        .map { movie ->
            if (movie == null) {
                MovieDetailUiState.Error
            } else {
                MovieDetailUiState.Success(movie)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MovieDetailUiState.Loading,
        )
}
