package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.local.StubData
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test

class SaveLocalSpellUseCaseTest {

    @MockK
    lateinit var repository: LocalRepository

    private lateinit var useCase: SaveLocalSpellUseCase
    private lateinit var testObserver: TestObserver<SpellsResult.LocalResult>
    private val localJsonSpells = StubData.spells
    private val testSpell = localJsonSpells.last()
    private val action = SpellsAction.SaveSpellAction(testSpell.toSpell())

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { repository.getSpells() } returns Single.just(localJsonSpells)
        useCase = SaveLocalSpellUseCase(repository)
        testObserver = useCase.performAction(action).test()
    }

    @Test
    fun `check that SaveSpellAction calls saveSpell on the localRepository`() {
        verify { repository.saveSpell(testSpell) }
    }

    @Test
    fun `check that SaveSpellAction returns correct LocalResult`() {
        testObserver.assertValue(SpellsResult.LocalResult(localJsonSpells.map { it.toSpell() }))
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }
}