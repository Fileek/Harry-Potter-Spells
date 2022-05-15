package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SearchAction
import com.epam.harrypotterspells.feature.main.MainResult.SearchResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SearchUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private val testString = "test"
    private lateinit var useCase: SearchUseCase
    private lateinit var actionComposer: ActionComposer<SearchAction, SearchResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SearchUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that OpenAction returns OpenResult`() {
        val testObserver = actionComposer(SearchAction.OpenAction)
        testObserver.assertValue(SearchResult.OpenResult)
    }

    @Test
    fun `check that QueryAction calls searchByQuery on the repository`() {
        actionComposer(SearchAction.QueryAction(testString))
        verify { repository.searchByQuery(testString) }
    }

    @Test
    fun `check that QueryAction returns QueryResult`() {
        val testObserver = actionComposer(SearchAction.QueryAction(testString))
        testObserver.assertValue(SearchResult.QueryResult)
    }

    @Test
    fun `check that CloseAction returns CloseResult`() {
        val testObserver = actionComposer(SearchAction.CloseAction)
        testObserver.assertValue(SearchResult.CloseResult)
    }
}