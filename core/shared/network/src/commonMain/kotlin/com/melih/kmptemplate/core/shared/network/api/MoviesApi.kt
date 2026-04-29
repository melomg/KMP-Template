package com.melih.kmptemplate.core.shared.network.api

import com.melih.kmptemplate.core.shared.model.Movies

interface MoviesApi {

    suspend fun getPopularMovies(page: Int): Movies?
}
