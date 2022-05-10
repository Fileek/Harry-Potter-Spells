package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditTypeAction
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditCreatorResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditEffectResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditIncantationResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditLightResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditTypeResult
import org.junit.Test

class EditSpellUseCaseTest {

    private val useCase = EditSpellUseCase()
    private val actionComposer = ActionComposer(useCase)

    @Test
    fun `check that EditIncantationAction returns EditIncantationResult`() {
        val testObserver = actionComposer(EditIncantationAction)
        testObserver.assertValue(EditIncantationResult)
    }

    @Test
    fun `check that EditTypeAction returns EditTypeResult`() {
        val testObserver = actionComposer(EditTypeAction)
        testObserver.assertValue(EditTypeResult)
    }

    @Test
    fun `check that EditEffectAction returns EditEffectResult`() {
        val testObserver = actionComposer(EditEffectAction)
        testObserver.assertValue(EditEffectResult)
    }

    @Test
    fun `check that EditLightAction returns EditLightResult`() {
        val testObserver = actionComposer(EditLightAction)
        testObserver.assertValue(EditLightResult)
    }

    @Test
    fun `check that EditCreatorAction returns EditCreatorResult`() {
        val testObserver = actionComposer(EditCreatorAction)
        testObserver.assertValue(EditCreatorResult)
    }
}