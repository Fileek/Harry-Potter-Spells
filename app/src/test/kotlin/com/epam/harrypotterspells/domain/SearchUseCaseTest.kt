package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SearchByQueryAction
import com.epam.harrypotterspells.feature.main.MainResult.SearchByQueryResult
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
    private lateinit var actionComposer: ActionComposer<SearchByQueryAction, SearchByQueryResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SearchUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that QueryAction calls searchByQuery on the repository`() {
        actionComposer(SearchByQueryAction(testString))
        verify { repository.searchByQuery(testString) }
    }

    @Test
    fun `check that QueryAction returns QueryResult`() {
        val testObserver = actionComposer(SearchByQueryAction(testString))
        testObserver.assertValue(SearchByQueryResult)
    }
}