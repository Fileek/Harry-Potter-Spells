package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateUseCaseTest {

    @MockK
    lateinit var localRepository: LocalRepository

    @MockK
    lateinit var remoteRepository: RemoteRepository

    private lateinit var useCase: UpdateUseCase
    private lateinit var actionComposer: ActionComposer<UpdateAction, UpdateResult>

    private val testString = "test"

    private val updateIncantationAction = UpdateAction.IncantationAction(testString, testString)
    private val updateTypeAction = UpdateAction.TypeAction(testString, testString)
    private val updateEffectAction = UpdateAction.EffectAction(testString, testString)
    private val updateLightAction = UpdateAction.LightAction(testString, testString)
    private val updateCreatorAction = UpdateAction.CreatorAction(testString, testString)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = UpdateUseCase(localRepository, remoteRepository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that IncantationAction calls updateIncantation on the repository`() {
        actionComposer(updateIncantationAction)
        verify { localRepository.updateIncantation(testString, testString) }
    }

    @Test
    fun `check that TypeAction calls updateType on the repository`() {
        actionComposer(updateTypeAction)
        verify { localRepository.updateType(testString, testString) }
    }

    @Test
    fun `check that EffectAction calls updateEffect on the repository`() {
        actionComposer(updateEffectAction)
        verify { localRepository.updateEffect(testString, testString) }
    }

    @Test
    fun `check that LightAction calls updateLight on the repository`() {
        actionComposer(updateLightAction)
        verify { localRepository.updateLight(testString, testString) }
    }

    @Test
    fun `check that CreatorAction calls updateCreator on the repository`() {
        actionComposer(updateCreatorAction)
        verify { localRepository.updateCreator(testString, testString) }
    }

    @Test
    fun `check that IncantationAction returns IncantationResult`() {
        val testObserver = actionComposer(updateIncantationAction)
        testObserver.assertValue(UpdateResult.IncantationResult(testString))
    }

    @Test
    fun `check that TypeAction returns TypeResult`() {
        val testObserver = actionComposer(updateTypeAction)
        testObserver.assertValue(UpdateResult.TypeResult(testString))
    }

    @Test
    fun `check that EffectAction returns EffectResult`() {
        val testObserver = actionComposer(updateEffectAction)
        testObserver.assertValue(UpdateResult.EffectResult(testString))
    }

    @Test
    fun `check that LightAction returns LightResult`() {
        val testObserver = actionComposer(updateLightAction)
        testObserver.assertValue(UpdateResult.LightResult(testString))
    }

    @Test
    fun `check that CreatorAction returns CreatorResult`() {
        val testObserver = actionComposer(updateCreatorAction)
        testObserver.assertValue(UpdateResult.CreatorResult(testString))
    }
}