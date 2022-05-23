package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.local.StubList
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.details.DetailsAction.SaveSpellAction
import com.epam.harrypotterspells.feature.details.DetailsResult.SaveSpellFieldResult
import com.epam.harrypotterspells.feature.details.SpellField
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test

class SaveSpellUseCaseTest {

    @MockK
    lateinit var localRepository: LocalRepository

    @MockK
    lateinit var remoteRepository: RemoteRepository

    private lateinit var useCase: SaveSpellUseCase
    private lateinit var testUseCasePerformer: TestUseCasePerformer<SaveSpellAction, SaveSpellFieldResult>
    private lateinit var testObserver: TestObserver<SaveSpellFieldResult>

    private val spell = StubList.spells.last().toSpell()

    private val saveSpellAction = SaveSpellAction(spell, SpellField.INCANTATION)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SaveSpellUseCase(localRepository, remoteRepository)
        testUseCasePerformer = TestUseCasePerformer(useCase)
        testObserver = testUseCasePerformer(saveSpellAction)
    }

    @Test
    fun `check that SaveSpellAction calls updateSpell on the localRepository`() {
        verify { localRepository.saveSpell(spell) }
    }

    @Test
    fun `check that SaveSpellAction calls updateSpell on the remoteRepository`() {
        verify { remoteRepository.saveSpell(spell) }
    }

    @Test
    fun `check that SaveSpellAction returns SaveSpellFieldResult`() {
        testObserver.assertValue(SaveSpellFieldResult(spell, SpellField.INCANTATION))
        testObserver.dispose()
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }
}