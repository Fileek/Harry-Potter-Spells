package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateTypeAction
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateSpellUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: UpdateSpellUseCase
    private lateinit var actionComposer: ActionComposer<UpdateSpellAction, UpdateSpellResult>

    private val testString = "test"

    private val updateIncantationAction = UpdateIncantationAction(testString, testString)
    private val updateTypeAction = UpdateTypeAction(testString, testString)
    private val updateEffectAction = UpdateEffectAction(testString, testString)
    private val updateLightAction = UpdateLightAction(testString, testString)
    private val updateCreatorAction = UpdateCreatorAction(testString, testString)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = UpdateSpellUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that UpdateIncantationAction calls updateIncantation on the repository`() {
        actionComposer(updateIncantationAction)
        verify { repository.updateIncantation(testString, testString) }
    }

    @Test
    fun `check that UpdateTypeAction calls updateType on the repository`() {
        actionComposer(updateTypeAction)
        verify { repository.updateType(testString, testString) }
    }

    @Test
    fun `check that UpdateEffectAction calls updateEffect on the repository`() {
        actionComposer(updateEffectAction)
        verify { repository.updateEffect(testString, testString) }
    }

    @Test
    fun `check that UpdateLightAction calls updateLight on the repository`() {
        actionComposer(updateLightAction)
        verify { repository.updateLight(testString, testString) }
    }

    @Test
    fun `check that UpdateCreatorAction calls updateCreator on the repository`() {
        actionComposer(updateCreatorAction)
        verify { repository.updateCreator(testString, testString) }
    }

    @Test
    fun `check that UpdateIncantationAction returns UpdateIncantationResult`() {
        val testObserver = actionComposer(updateIncantationAction)
        testObserver.assertValue(UpdateSpellResult.UpdateIncantationResult(testString))
    }

    @Test
    fun `check that UpdateTypeAction returns UpdateTypeResult`() {
        val testObserver = actionComposer(updateTypeAction)
        testObserver.assertValue(UpdateSpellResult.UpdateTypeResult(testString))
    }

    @Test
    fun `check that UpdateEffectAction returns UpdateEffectResult`() {
        val testObserver = actionComposer(updateEffectAction)
        testObserver.assertValue(UpdateSpellResult.UpdateEffectResult(testString))
    }

    @Test
    fun `check that UpdateLightAction returns UpdateLightResult`() {
        val testObserver = actionComposer(updateLightAction)
        testObserver.assertValue(UpdateSpellResult.UpdateLightResult(testString))
    }

    @Test
    fun `check that UpdateCreatorAction returns UpdateCreatorResult`() {
        val testObserver = actionComposer(updateCreatorAction)
        testObserver.assertValue(UpdateSpellResult.UpdateCreatorResult(testString))
    }
}