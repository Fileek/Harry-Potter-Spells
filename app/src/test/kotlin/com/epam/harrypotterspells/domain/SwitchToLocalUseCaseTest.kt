package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction.ToLocalAction
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult.ToLocalResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SwitchToLocalUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: SwitchSourceUseCase
    private lateinit var actionComposer: ActionComposer<ToLocalAction, ToLocalResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SwitchSourceUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that SwitchToLocalAction calls switchToLocal on the repository`() {
        actionComposer(ToLocalAction)
        verify { repository.switchToLocal() }
    }

    @Test
    fun `check that SwitchToLocalAction returns SwitchToLocalResult`() {
        val testObserver = actionComposer(ToLocalAction)
        testObserver.assertValue(ToLocalResult)
    }
}