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
import org.junit.Before
import org.junit.Test

class SaveSpellUseCaseTest {

    @MockK
    lateinit var localRepository: LocalRepository

    @MockK
    lateinit var remoteRepository: RemoteRepository

    private lateinit var useCase: SaveSpellUseCase
    private lateinit var testUseCasePerformer: TestUseCasePerformer<SaveSpellAction, SaveSpellFieldResult>

    private val spell = StubList.spells.last().toSpell()

    private val saveSpellAction = SaveSpellAction(spell, SpellField.INCANTATION)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SaveSpellUseCase(localRepository, remoteRepository)
        testUseCasePerformer = TestUseCasePerformer(useCase)
    }

    @Test
    fun `check that SaveSpellAction calls updateSpell on the localRepository`() {
        testUseCasePerformer(saveSpellAction)
        verify { localRepository.saveSpell(spell) }
    }

    @Test
    fun `check that SaveSpellAction calls updateSpell on the remoteRepository`() {
        testUseCasePerformer(saveSpellAction)
        verify { remoteRepository.saveSpell(spell) }
    }

    @Test
    fun `check that SaveSpellAction returns SaveSpellFieldResult`() {
        val testObserver = testUseCasePerformer(saveSpellAction)
        testObserver.assertValue(SaveSpellFieldResult(spell, SpellField.INCANTATION))
    }
}