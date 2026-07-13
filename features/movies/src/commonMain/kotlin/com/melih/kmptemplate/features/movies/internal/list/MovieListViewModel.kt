package com.melih.kmptemplate.features.movies.internal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.model.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class MovieListViewModel(moviesRepository: MoviesRepository) : ViewModel() {
    val movies: StateFlow<List<Movie>> =
        moviesRepository.getMovies()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
