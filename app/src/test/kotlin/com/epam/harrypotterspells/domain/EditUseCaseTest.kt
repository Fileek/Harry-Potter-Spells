package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.feature.details.DetailsAction
import com.epam.harrypotterspells.feature.details.DetailsResult
import org.junit.Test

class EditUseCaseTest {

    private val useCase = EditUseCase()
    private val actionComposer = ActionComposer(useCase)

    @Test
    fun `check that IncantationAction returns IncantationResult`() {
        val testObserver = actionComposer(DetailsAction.EditAction.IncantationAction)
        testObserver.assertValue(DetailsResult.EditResult.IncantationResult)
    }

    @Test
    fun `check that TypeAction returns TypeResult`() {
        val testObserver = actionComposer(DetailsAction.EditAction.TypeAction)
        testObserver.assertValue(DetailsResult.EditResult.TypeResult)
    }

    @Test
    fun `check that EffectAction returns EffectResult`() {
        val testObserver = actionComposer(DetailsAction.EditAction.EffectAction)
        testObserver.assertValue(DetailsResult.EditResult.EffectResult)
    }

    @Test
    fun `check that LightAction returns LightResult`() {
        val testObserver = actionComposer(DetailsAction.EditAction.LightAction)
        testObserver.assertValue(DetailsResult.EditResult.LightResult)
    }

    @Test
    fun `check that CreatorAction returns CreatorResult`() {
        val testObserver = actionComposer(DetailsAction.EditAction.CreatorAction)
        testObserver.assertValue(DetailsResult.EditResult.CreatorResult)
    }
}