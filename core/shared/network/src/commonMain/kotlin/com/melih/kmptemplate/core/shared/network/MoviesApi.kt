package com.melih.kmptemplate.core.shared.network

import com.melih.kmptemplate.core.shared.model.Movies

interface MoviesApi {

    suspend fun getPopularMovies(page: Int): Movies?
}
