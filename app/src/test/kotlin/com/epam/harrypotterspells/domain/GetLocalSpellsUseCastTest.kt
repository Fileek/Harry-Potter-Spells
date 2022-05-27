package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.local.StubData
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetLocalSpellsUseCastTest {

    @MockK
    lateinit var repo: LocalRepository

    private lateinit var testObserver: TestObserver<SpellsResult.LocalResult>
    private lateinit var useCase: GetLocalSpellsUseCase
    private val spells = StubData.spells

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { repo.getSpells() } returns Single.just(spells)
        useCase = GetLocalSpellsUseCase(repo)
    }

    @Test
    fun `check that GetLocalAction returns correct LocalResult`() {
        testObserver = Observable.just(SpellsAction.GetLocalAction)
            .compose(useCase.performAction())
            .test()
        testObserver.assertValue(SpellsResult.LocalResult(spells.map { it.toSpell() }))
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }
}