package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction.CreatorAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction.EffectAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction.IncantationAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction.LightAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction.TypeAction
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateSpellUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: UpdateSpellUseCase
    private lateinit var actionComposer: ActionComposer<UpdateAction, UpdateResult>

    private val testString = "test"

    private val updateIncantationAction = IncantationAction(testString, testString)
    private val updateTypeAction = TypeAction(testString, testString)
    private val updateEffectAction = EffectAction(testString, testString)
    private val updateLightAction = LightAction(testString, testString)
    private val updateCreatorAction = CreatorAction(testString, testString)

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
        testObserver.assertValue(UpdateResult.IncantationResult(testString))
    }

    @Test
    fun `check that UpdateTypeAction returns UpdateTypeResult`() {
        val testObserver = actionComposer(updateTypeAction)
        testObserver.assertValue(UpdateResult.TypeResult(testString))
    }

    @Test
    fun `check that UpdateEffectAction returns UpdateEffectResult`() {
        val testObserver = actionComposer(updateEffectAction)
        testObserver.assertValue(UpdateResult.EffectResult(testString))
    }

    @Test
    fun `check that UpdateLightAction returns UpdateLightResult`() {
        val testObserver = actionComposer(updateLightAction)
        testObserver.assertValue(UpdateResult.LightResult(testString))
    }

    @Test
    fun `check that UpdateCreatorAction returns UpdateCreatorResult`() {
        val testObserver = actionComposer(updateCreatorAction)
        testObserver.assertValue(UpdateResult.CreatorResult(testString))
    }
}