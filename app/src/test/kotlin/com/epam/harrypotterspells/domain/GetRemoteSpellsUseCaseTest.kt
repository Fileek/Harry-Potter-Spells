package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.StubData
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import java.net.ConnectException
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRemoteSpellsUseCaseTest {

    @MockK
    lateinit var repo: RemoteRepository

    private lateinit var testObserver: TestObserver<SpellsResult.RemoteResult>
    private lateinit var useCase: GetRemoteSpellsUseCase
    private val remoteJsonSpells = StubData.spells.take(3)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { repo.getSpells() } returns Single.just(remoteJsonSpells)
        useCase = GetRemoteSpellsUseCase(repo)
    }

    @Test
    fun `check that GetRemoteAction starts with Loading result`() {
        testObserver = Observable.just(SpellsAction.GetRemoteAction)
            .compose(useCase.performAction())
            .test()
        testObserver.assertValueAt(FIRST_RESULT_INDEX, SpellsResult.RemoteResult.Loading)
    }

    @Test
    fun `check that GetRemoteAction returns Success result on success`() {
        testObserver = Observable.just(SpellsAction.GetRemoteAction)
            .compose(useCase.performAction())
            .test()
        testObserver.assertValueAt(
            SECOND_RESULT_INDEX,
            SpellsResult.RemoteResult.Success(remoteJsonSpells.map { it.toSpell() })
        )
    }

    @Test
    fun `check that GetRemoteAction returns error result on error`() {
        val error = ConnectException()
        every { repo.getSpells() } returns Single.error(error)
        testObserver = Observable.just(SpellsAction.GetRemoteAction)
            .compose(useCase.performAction())
            .test()
        testObserver.assertValueAt(SECOND_RESULT_INDEX, SpellsResult.RemoteResult.Error(error))
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }

    private companion object {
        private const val FIRST_RESULT_INDEX = 0
        private const val SECOND_RESULT_INDEX = 1
    }
}