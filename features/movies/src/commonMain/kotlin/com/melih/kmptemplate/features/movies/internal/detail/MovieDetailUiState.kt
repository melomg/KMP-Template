package com.melih.kmptemplate.features.movies.internal.detail

import com.melih.kmptemplate.core.shared.model.Movie

internal sealed interface MovieDetailUiState {

    data object Loading : MovieDetailUiState

    data class Success(val movie: Movie) : MovieDetailUiState

    data object Error : MovieDetailUiState
}
