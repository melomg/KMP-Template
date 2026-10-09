package com.melih.kmptemplate.features.movies.internal.detail

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.core.shared.test.utils.launchInBackground
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.koin.java.KoinJavaComponent.inject
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal val MovieDetailViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin()
) {
    testFixture {
        object {
            val repository: TestMoviesRepository by inject(TestMoviesRepository::class.java)
            val viewModel = MovieDetailViewModel(1L, repository)
        }
    } asContextForEach {
        test("initial state is Loading") {
            assertEquals(
                expected = MovieDetailUiState.Loading,
                actual = viewModel.uiState.value,
            )

            assertEquals(
                expected = 1L,
                actual = repository.requestedMovieId,
            )
        }

        test("emits Success when repository returns a movie") {
            val movie = createFakeMovie(id = 1L, title = "Inception")
            repository.setMovies(listOf(movie))

            val states = mutableListOf<MovieDetailUiState>()
            it.testScope.launchInBackground {
                viewModel.uiState.toList(states)
            }

            assertEquals(
                expected = listOf(
                    MovieDetailUiState.Loading,
                    MovieDetailUiState.Success(movie),
                ),
                actual = states,
            )
            assertIs<MovieDetailUiState.Success>(viewModel.uiState.value)
            assertEquals(movie, (viewModel.uiState.value as MovieDetailUiState.Success).movie)
        }

        test("emits Error when repository returns null") {
            val differentMovie = createFakeMovie(id = 2L, title = "Inception")
            repository.setMovies(listOf(differentMovie))

            val states = mutableListOf<MovieDetailUiState>()
            it.testScope.launchInBackground {
                viewModel.uiState.toList(states)
            }

            assertEquals(
                expected = listOf(
                    MovieDetailUiState.Loading,
                    MovieDetailUiState.Error,
                ),
                actual = states,
            )
            assertEquals(
                expected = MovieDetailUiState.Error,
                actual = viewModel.uiState.value,
            )
        }

        test("updates state dynamically when repository emits new values") {
            val movie1 = createFakeMovie(id = 1L, title = "Movie 1")
            val movie2 = createFakeMovie(id = 1L, title = "Movie 2")

            val states = mutableListOf<MovieDetailUiState>()
            it.testScope.launchInBackground {
                viewModel.uiState.toList(states)
            }

            repository.addMovie(movie1)
            it.testScope.advanceUntilIdle()
            repository.addMovie(movie2)
            it.testScope.advanceUntilIdle()

            assertEquals(
                expected = listOf(
                    MovieDetailUiState.Loading,
                    MovieDetailUiState.Success(movie1),
                    MovieDetailUiState.Success(movie2),
                ),
                actual = states,
            )
        }
    } closeWith {
        repository.setMovies(emptyList())
    }
}

private fun createFakeMovie(
    id: Long = 1L,
    title: String = "Test Movie",
    overview: String = "Test Overview",
    backdropUrl: String = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
    posterUrl: String = "https://image.tmdb.org/t/p/w500/poster.jpg",
    adult: Boolean = false,
    originalLanguage: String = "en",
    originalTitle: String = "Test Movie",
    popularity: Float = 8.5f,
    releaseDate: String = "2024-01-01",
    video: Boolean = false,
    voteAverage: String = "8.5",
    voteCount: Int = 100,
) = Movie(
    adult = adult,
    backdropUrl = backdropUrl,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)
