package com.melih.kmptemplate.features.movies.internal.detail

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.model.fakeMovie
import com.melih.kmptemplate.core.shared.test.utils.launchInBackground
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal val MovieDetailViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin()
) {
    testFixture {
        object : KoinTest {
            val repository: TestMoviesRepository by inject()
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
            val movie = fakeMovie(id = 1L, title = "Inception")
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
            val differentMovie = fakeMovie(id = 2L, title = "Inception")
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
            val movie1 = fakeMovie(id = 1L, title = "Movie 1")
            val movie2 = fakeMovie(id = 1L, title = "Movie 2")

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
