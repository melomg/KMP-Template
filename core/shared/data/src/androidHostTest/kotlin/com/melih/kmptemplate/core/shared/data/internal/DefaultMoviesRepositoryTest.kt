package com.melih.kmptemplate.core.shared.data.internal

import com.melih.kmptemplate.core.shared.data.api.di.Qualifiers
import com.melih.kmptemplate.core.shared.data.internal.local.InMemoryMoviesDataSource
import com.melih.kmptemplate.core.shared.data.internal.remote.RemoteMoviesDataSource
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.core.shared.model.fakeMovie
import com.melih.kmptemplate.core.shared.test.utils.launchInBackground
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
internal val DefaultMoviesRepositoryTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin()
) {
    testFixture {
        object : KoinTest {
            val applicationScope: CoroutineScope by inject(Qualifiers.APPLICATION_SCOPE)
            val remote = mockk<RemoteMoviesDataSource>()
            val cache = mockk<InMemoryMoviesDataSource>(relaxed = true)
            val repository = DefaultMoviesRepository(
                applicationScope = applicationScope,
                remote = remote,
                cache = cache,
            )

            init {
                coEvery { remote.getPopularMovies(any()) } returns MOVIES
                every { cache.getMovies() } returns flowOf(MOVIES)
            }
        }
    } asContextForEach {
        test("init triggers refresh and saves fetched movies into cache") {
            // When
            val expectedMovies = mutableListOf<List<Movie>>()
            it.testScope.launchInBackground {
                repository.getMovies().toList(expectedMovies)
            }

            // Then
            assertEquals(expected = listOf(MOVIES), actual = expectedMovies)
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(MOVIES)
                cache.getMovies()
            }
        }

        test("refresh calls remote with first page and saves movies") {
            // Given
            val movie1 = fakeMovie(id = 10L, title = "Interstellar")
            val movie2 = fakeMovie(id = 20L, title = "Dunkirk")
            val newMovies = listOf(movie1, movie2)
            coEvery { remote.getPopularMovies(any()) } returns newMovies

            // When
            repository.refresh()

            // Then
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(MOVIES)
                remote.getPopularMovies(1)
                cache.saveMovies(newMovies)
            }
        }

        test("refresh does not save to cache when remote returns null") {
            // Given
            coEvery { remote.getPopularMovies(any()) } returns null

            // When
            repository.refresh()

            // Then
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(MOVIES)
                remote.getPopularMovies(1)
            }
        }

        test("getMovies delegates to cache getMovies") {
            // Given
            clearAllMocks()
            val expectedFlow = flowOf(MOVIES)
            every { cache.getMovies() } returns expectedFlow

            // When
            val actualFlow = repository.getMovies()

            val emissions = mutableListOf<List<Movie>>()
            it.testScope.launchInBackground {
                actualFlow.toList(emissions)
            }

            // Then
            assertEquals(expected = expectedFlow, actual = actualFlow)
            assertEquals(expected = listOf(MOVIES), actual = emissions)
            coVerifySequence {
                cache.getMovies()
            }
        }

        test("getMovieById delegates to cache getMovieById") {
            // Given
            val movieId = 42L
            val movie = fakeMovie(id = movieId, title = "The Matrix")
            val expectedFlow = flowOf(movie)
            every { cache.getMovieById(movieId) } returns expectedFlow

            // When
            val actualFlow = repository.getMovieById(movieId)

            val emissions = mutableListOf<Movie?>()
            it.testScope.launchInBackground {
                actualFlow.toList(emissions)
            }

            // Then
            assertEquals(expected = expectedFlow, actual = actualFlow)
            assertEquals(expected = listOf<Movie?>(movie), actual = emissions)
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(MOVIES)
                cache.getMovieById(movieId)
            }
        }

        test("getMovieById returns null when movie is not found") {
            // Given
            val movieId = 999L
            val expectedFlow = flowOf<Movie?>(null)
            every { cache.getMovieById(movieId) } returns expectedFlow

            // When
            val actualFlow = repository.getMovieById(movieId)

            val emissions = mutableListOf<Movie?>()
            it.testScope.launchInBackground {
                actualFlow.toList(emissions)
            }

            // Then
            assertEquals(expected = expectedFlow, actual = actualFlow)
            assertEquals(expected = listOf<Movie?>(null), actual = emissions)
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(MOVIES)
                cache.getMovieById(movieId)
            }
        }

        test("multiple refreshes update the cache sequentially") {
            // Given
            clearAllMocks()
            val batch1 = listOf(fakeMovie(id = 1L, title = "Batch 1"))
            val batch2 = listOf(fakeMovie(id = 2L, title = "Batch 2"))

            coEvery { remote.getPopularMovies(1) } returns batch1 andThen batch2

            // When
            repository.refresh()
            repository.refresh()

            // Then
            coVerifySequence {
                remote.getPopularMovies(1)
                cache.saveMovies(batch1)
                remote.getPopularMovies(1)
                cache.saveMovies(batch2)
            }
        }
    } closeWith {
        confirmVerified(
            remote,
            cache,
        )
        clearAllMocks()
    }
}

private val MOVIES = listOf(fakeMovie(id = 1L, title = "Inception"))
