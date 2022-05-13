package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction.EditCreatorAction
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction.EditEffectAction
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction.EditIncantationAction
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction.EditLightAction
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction.EditTypeAction
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult.CreatorResult
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult.EffectResult
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult.IncantationResult
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult.LightResult
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult.TypeResult
import org.junit.Test

class EditUseCaseTest {

    private val useCase = EditUseCase()
    private val actionComposer = ActionComposer(useCase)

    @Test
    fun `check that EditIncantationAction returns EditIncantationResult`() {
        val testObserver = actionComposer(EditIncantationAction)
        testObserver.assertValue(IncantationResult)
    }

    @Test
    fun `check that EditTypeAction returns EditTypeResult`() {
        val testObserver = actionComposer(EditTypeAction)
        testObserver.assertValue(TypeResult)
    }

    @Test
    fun `check that EditEffectAction returns EditEffectResult`() {
        val testObserver = actionComposer(EditEffectAction)
        testObserver.assertValue(EffectResult)
    }

    @Test
    fun `check that EditLightAction returns EditLightResult`() {
        val testObserver = actionComposer(EditLightAction)
        testObserver.assertValue(LightResult)
    }

    @Test
    fun `check that EditCreatorAction returns EditCreatorResult`() {
        val testObserver = actionComposer(EditCreatorAction)
        testObserver.assertValue(CreatorResult)
    }
}